package com.netty.pool4.halb;

import com.netty.pool4.client.NettyClientProxy;
import com.netty.pool4.vo.HeatBeatMsg;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import string.utils.BlankUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/8/2.
 */
@Component
public class ServiceAddress implements InitializingBean{

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceAddress.class);

    private static ConcurrentHashMap<String, List<ServerInfo>> activeAddress = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, List<ServerInfo>> inactiveAddress = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, List<ServerInfo>> receiveAddress = new ConcurrentHashMap<>();

    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    private final int DEFAULT_WEIGHT = 1;

    private final String DEFAULT_SERVICE_NAME = "biz";

    @Autowired
    private NettyClientProxy proxy;

    @Autowired
    private HeartBeatService heartBeatService;

    public void initServiceName(String serviceName) {
        if (!activeAddress.containsKey(serviceName)){
            activeAddress.put(serviceName, new ArrayList<>());
        }
        if (!inactiveAddress.containsKey(serviceName)) {
            inactiveAddress.put(serviceName, new ArrayList<>());
        }
        if (!receiveAddress.containsKey(serviceName)) {
            receiveAddress.put(serviceName, new ArrayList<>());
        }
    }

    public void addService(String ip, int port, int weight, String serviceName) {
        initServiceName(serviceName);
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp(ip);
        serverInfo.setPort(port);
        serverInfo.setWeight(weight);
        activeAddress.get(getServiceName(serviceName)).add(serverInfo);
    }

    public InetSocketAddress getInetAddress(ServerInfo serverInfo) {
        InetSocketAddress address = new InetSocketAddress(serverInfo.getIp(), serverInfo.getPort());
        return address;
    }

    public InetSocketAddress getAvailAddress(String serviceName) {
        if (activeAddress.containsKey(getServiceName(serviceName))) {
            List<ServerInfo> temp = new ArrayList<>();
            List<ServerInfo> list = activeAddress.get(getServiceName(serviceName));
            if (null != list && 0 < list.size()) {
                for (ServerInfo info:list) {
                    for (int num = 0; num < (0==info.getWeight()?DEFAULT_WEIGHT:info.getWeight()); num++){
                        temp.add(info);
                    }
                }
                Collections.shuffle(temp);
                ServerInfo info = temp.get(0);
                InetSocketAddress address = new InetSocketAddress(info.getIp(), info.getPort());
                return address;
            }
        }
        return null;
    }

    synchronized
    public void inactive(ServerInfo serverInfo) {
        Set<String> sets = activeAddress.keySet();
        for (String serviceName :
                sets) {
            inactive(serverInfo, serviceName);
        }
    }

    synchronized
    public void inactive(ServerInfo serverInfo, String serviceName) {
        activeAddress.get(getServiceName(serviceName)).remove(serverInfo);
        if(!inactiveAddress.get(getServiceName(serviceName)).contains(serverInfo))
            inactiveAddress.get(getServiceName(serviceName)).add(serverInfo);
    }

    synchronized
    public void receive(HeatBeatMsg msg) {
        ServerInfo serverInfo = msg.getServerInfo();
        LOGGER.info("serviceAddress receive:"+msg+",receiveAddress:"+receiveAddress);
        if(!receiveAddress.get(getServiceName(msg.getServiceName())).contains(serverInfo))
            receiveAddress.get(getServiceName(msg.getServiceName())).add(serverInfo);
    }

    synchronized
    public void active(ServerInfo serverInfo, String serviceName) {
        inactiveAddress.get(getServiceName(serviceName)).remove(serverInfo);
        //if (!activeAddress.containsKey(getServiceName(serviceName))){
        if(!activeAddress.get(getServiceName(serviceName)).contains(serverInfo))
            activeAddress.get(getServiceName(serviceName)).add(serverInfo);
        //}
    }

    public void retry() {
        scheduledExecutorService.scheduleAtFixedRate(new Thread(){
            public void run() {
                try {
                    LOGGER.info("serverAddress retry begin:"+inactiveAddress);
                    ConcurrentHashMap<String, List<ServerInfo>> failList = inactiveAddress;
                    Set<String> serviceSet = failList.keySet();//address.keySet();
                    for (String service :
                            serviceSet) {
                        List<ServerInfo> infos = inactiveAddress.get(service);
                        if (null != infos && 0 < infos.size()) {
                            List<ServerInfo> list = new ArrayList<ServerInfo>();
                            for (ServerInfo info :
                                    infos) {
                                try {
                                    InetSocketAddress addr1 = new InetSocketAddress(info.getIp(), info.getPort());
                                    Channel channel = proxy.getClient(addr1);
                                    channel.writeAndFlush(HeatBeatMsg.getHeartBeatReq(info, service));
                                    list.add(info);
                                    if (0 == infos.size()) {
                                        break;
                                    }
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            if (null != list && 0 < list.size()) {
                                for (ServerInfo info :
                                        list) {
                                    active(info, service);
                                }
                            }
                        }
                    }
                    LOGGER.warn("serverAddress retry end");
                } catch (Exception e) {
                    LOGGER.warn("serverAddress retry error", e);
                }
            }
        }, 60, 60, TimeUnit.SECONDS);//10*60
    }

    public String getServiceName(String serviceName) {
        return BlankUtil.isBlank(serviceName)?DEFAULT_SERVICE_NAME:serviceName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            LOGGER.info("ServiceAddress init begin");
            this.addService("127.0.0.1", 8888, 1, "biz");
            this.addService("127.0.0.1", 8889, 1, "biz");
            this.addService("127.0.0.1", 8888, 1, "biz2");
            this.addService("127.0.0.1", 8889, 1, "biz2");
            ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);
            scheduledExecutorService.scheduleAtFixedRate(new Thread(){
                public void run(){
                    heartBeatService.heartbeat();
                }
            }, 10, 60, TimeUnit.SECONDS);//每隔多少时间发送心跳
            retry();//每隔10分钟重试失败的连接
            LOGGER.info("ServiceAddress init success");
        } catch (Exception e) {
            LOGGER.warn("ServiceAddress init error", e);
        }
    }

    public ConcurrentHashMap<String, List<ServerInfo>> getActiveAddress() {
        return activeAddress;
    }

    public ConcurrentHashMap<String, List<ServerInfo>> getReceiveAddress() {
        return receiveAddress;
    }

    public ConcurrentHashMap<String, List<ServerInfo>> getInactiveAddress() {
        return inactiveAddress;
    }
}

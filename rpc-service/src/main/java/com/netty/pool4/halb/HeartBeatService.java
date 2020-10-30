package com.netty.pool4.halb;

import com.netty.pool4.client.NettyClientProxy;
import com.netty.pool4.vo.HeatBeatMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/1.
 */
@Component
public class HeartBeatService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HeartBeatService.class);

    private CountDownLatch wait = new CountDownLatch(1);

    @Autowired
    private ServiceAddress serviceAddress;

    @Autowired
    private NettyClientProxy proxy;

    public void heartbeat() {
        try {
            LOGGER.info("heartbeat begin");
            ConcurrentHashMap<String, List<ServerInfo>> address = serviceAddress.getActiveAddress();
            Set<String> serviceSet = address.keySet();

            sendHeartBeat(address, serviceSet);
            LOGGER.info("heartbeat await");
            wait.await(10, TimeUnit.SECONDS);
            refreshConnection(address);
        } catch (Exception e) {
            LOGGER.warn("heartbeat error", e);
        }
        LOGGER.info("heartbeat end");
    }

    synchronized
    private void sendHeartBeat(ConcurrentHashMap<String, List<ServerInfo>> address, Set<String> serviceSet) {
        for (String service :
                serviceSet) {
            List<ServerInfo> failInfo = new ArrayList<>();
            List<ServerInfo> infos = address.get(service);
            if (null != infos && 0 < infos.size()) {
                try {
                    for (int i = 0; i < infos.size() && infos.size() > 0; i++) {
                        ServerInfo info = infos.get(i);
                        try {
                            LOGGER.info("heartbeat ing service:" + service + "," + info.toString());
                            proxy.getClient(serviceAddress.getInetAddress(info)).writeAndFlush((HeatBeatMsg.getHeartBeatReq(info, service)));
                        } catch (Exception e) {
                            LOGGER.warn("heartbeat error:" + info.toString(), e);
                            failInfo.add(info);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn("heartbeat error infosize", e);
                }
            }
            for (ServerInfo info :
                    failInfo) {
                serviceAddress.inactive(info, service);
            }
        }
    }

    synchronized
    private void refreshConnection(ConcurrentHashMap<String, List<ServerInfo>> address) {
        //可用列表迁移
        address = serviceAddress.getReceiveAddress();
        Set<String> serviceSet = address.keySet();
        for (String service :
                serviceSet) {
            List<ServerInfo> infos = address.get(service);
            if (null != infos && 0 < infos.size()) {
                for (ServerInfo info :
                        infos) {
                    serviceAddress.active(info, service);
                }
            }
        }
        LOGGER.info("可用连接："+serviceAddress.getActiveAddress()+",不可用连接："+serviceAddress.getInactiveAddress()+",收到的连接："+serviceAddress.getReceiveAddress());

        //serviceAddress.getReceiveAddress().clear();空指针
        for (String service :
                serviceSet) {
            serviceAddress.getReceiveAddress().get(service).clear();
            serviceAddress.getReceiveAddress().put(service, new ArrayList< >());
        }
    }
}

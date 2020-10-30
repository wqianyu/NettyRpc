package com.netty.pool4.client;

import com.netty.pool4.halb.ServerInfo;
import com.netty.pool4.halb.ServiceAddress;
import com.netty.pool4.vo.RpcRequest;
import com.netty.pool4.vo.RpcResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/20.
 */
@Component
public class NettyClient {

    @Autowired
    private NettyClientProxy proxy;

    @Autowired
    private ServiceAddress serviceAddress;

    private final Integer maxTimeout = 10;

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private static ConcurrentHashMap<String, RpcResponse> receiveList = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, CountDownLatch> lock = new ConcurrentHashMap<>();

    //private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    public static RpcResponse getResponseData(RpcRequest request) {
        if (receiveList.containsKey(request.getMessageId())) {
            return receiveList.get(request.getMessageId());
        }
        return null;
    }

    public static void addResponse(RpcResponse response) {
        receiveList.put(response.getMessageId(), response);
    }

    public static void removeRsp(RpcResponse response) {
        receiveList.remove(response.getMessageId());
    }

    public static boolean isContains(RpcResponse response) {
        return receiveList.containsKey(response.getMessageId());
    }

    public RpcResponse send(String serviceName, RpcRequest request) {

        boolean suc = false;
        RpcResponse response = null;
        InetSocketAddress address = null;
        try {
            address = serviceAddress.getAvailAddress(serviceName);
            if(null == address) {
                throw new Exception("channel不可用，无可用链接地址");
            }
            Channel channel = proxy.getClient(address);
            /*if(!channel.isActive()){
                throw new Exception("channel不可用，请确认连接");
            }*///每次使用channel一次都会aquire一次
            lock.put(request.getMessageId(), new CountDownLatch(1));
            channel.writeAndFlush(request);
            suc = true;
            LOGGER.info("_________waiting_______");
            long begin = System.currentTimeMillis();
            lock.get(request.getMessageId()).await(maxTimeout, TimeUnit.SECONDS);//阻塞，等待接收时候唤醒或者超时唤醒
            LOGGER.info("_________active_______"+lockSize());

            boolean state = false;
            response = getResponseData(request);
            if (null != response) {
                state = true;
            }
            if (!state) {//超时抛出异常
                throw new Exception("time out");
            }
        } catch (InterruptedException e) {
            LOGGER.warn("send error InterruptedException", e);
        } catch (Exception e) {
            LOGGER.warn("send error ExecutionException", e);
        } finally {
            //LOGGER.info(" ====== finally begin===== "+lockSize()+ " "+size());
            if (islock(request.getMessageId())) {
                lock.remove(request.getMessageId());
            }
            if (null != response && isContains(response)) {//清理过期的数据
                removeRsp(response);
            }
            if(!suc && null != address) {
                serviceAddress.inactive(new ServerInfo(address.getHostName(), address.getPort()), serviceName);
            }
            //LOGGER.info(" ====== finally end  ===== "+lockSize()+ " "+size());
        }
        return response;
    }

    public static void callNotify(String messageId) {//接收消息线程唤醒处理线程
        CountDownLatch object = lock.get(messageId);
        if (null != object) {
            LOGGER.info("_________notify_______");
            object.countDown();
        } else {
            LOGGER.info("_________no found_______");
        }
    }

    public static int size() {
        return receiveList.size();
    }

    public static int lockSize() {
        return lock.size();
    }

    public static boolean islock(String messageId) {
        return lock.containsKey(messageId);
    }
}

package com.netty.pool3.countDownClient;

import com.netty.pool3.common.RpcRequest;
import com.netty.pool3.common.RpcResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/7/20.
 */
public class NettyClient {

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

    public RpcResponse send(Channel channel, RpcRequest request) {

        RpcResponse response = null;
        ScheduledExecutorService scheduledExecutorService = null;
        try {
            if(!channel.isActive()){
                throw new Exception("channel不可用，请确认连接");
            }
            lock.put(request.getMessageId(), new CountDownLatch(1));
            channel.writeAndFlush(request);
            LOGGER.info("_________waiting_______");
            long begin = System.currentTimeMillis();
            lock.get(request.getMessageId()).await(maxTimeout, TimeUnit.SECONDS);//阻塞，等待接收时候唤醒或者超时唤醒
            LOGGER.info("_________active_______"+lockSize());

            boolean state = false;
            while(!state && (maxTimeout*1000 > System.currentTimeMillis()-begin)) {//超时时间内监听返回结果（只执行一次，已经有返回结果或者已超时）
                response = getResponseData(request);
                if (null == response) {
                    Thread.sleep(10);//为空延迟10ms再获取
                    continue;
                } else {
                    state = true;
                }
            }
            if (!state) {//超时抛出异常
                throw new Exception("time out");
            }
        } catch (InterruptedException e) {
            LOGGER.warn("send error InterruptedException", e);
        } catch (Exception e) {
            LOGGER.warn("send error ExecutionException", e);
        } finally {
            LOGGER.info(" ====== finally begin===== "+lockSize()+ " "+size());
            if (islock(request.getMessageId())) {
                lock.remove(request.getMessageId());
            }
            if (null != response && isContains(response)) {//清理过期的数据
                removeRsp(response);
            }
            LOGGER.info(" ====== finally end  ===== "+lockSize()+ " "+size());
            if (null != scheduledExecutorService) {
                scheduledExecutorService.shutdown();
            }
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

package com.netty.pool7.client;

import com.netty.pool7.vo.RpcRequest;
import com.netty.pool7.vo.RpcResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/20.
 */
public class NettyClient {

    private final Integer maxTimeout = 10;

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private static ConcurrentHashMap<String, SyncFuture<RpcResponse>> receiveList = new ConcurrentHashMap<>();

    private NettyClientProxy proxy = NettyClientProxy.getProxy().initMaxSize(2);//通过address获取连接池连接

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    public RpcResponse send(InetSocketAddress address, RpcRequest request) {

        RpcResponse response = null;
        try {
            Channel channel = proxy.build(address);
            SyncFuture<RpcResponse> future = new SyncFuture<>();//新建请求实例
            receiveList.put(request.getMessageId(), future);//放到队列
            channel.writeAndFlush(request);
            LOGGER.info("_________waiting_______");
            response = future.get(maxTimeout, TimeUnit.SECONDS);//阻塞，等待接收时候唤醒或者超时唤醒
            LOGGER.info("_________active_______"+size());
            if (null == response) {
                throw new Exception("time out");//maxTimeout时间到了还是没有收到返回值
            }
        } catch (InterruptedException e) {
            LOGGER.warn("send error InterruptedException", e);
        } catch (Exception e) {
            LOGGER.warn("send error ExecutionException", e);
        } finally {
            /*if (null != response && isContains(response)) {//清理过期/已接收的数据，防止队列过大
                removeRsp(response);
            }*/
            LOGGER.info("_________active_______"+size());
        }
        return response;
    }

    public SyncFuture sendAsync(InetSocketAddress address, RpcRequest request) {
        SyncFuture<RpcResponse> reqfuture = new SyncFuture<>();//新建请求实例
        try {
            Channel channel = proxy.build(address);
            receiveList.put(request.getMessageId(), reqfuture);//放到队列
            channel.writeAndFlush(request);
            return reqfuture;
        } catch (InterruptedException e) {
            LOGGER.warn("send error InterruptedException", e);
        } catch (Exception e) {
            LOGGER.warn("send error ExecutionException", e);
        }
        return reqfuture;
    }

    public static void callNotify(String messageId, RpcResponse response) {//接收消息线程唤醒处理线程
        SyncFuture<RpcResponse> future = receiveList.get(messageId);
        if (null != future) {
            LOGGER.info("_________notify_______");
            removeRsp(response);
            future.setResponse(response);
        } else {
            LOGGER.info("_________no found_______");
        }
    }

    public static int size() {
        return receiveList.size();
    }

    public static void removeRsp(String messageId) {
        receiveList.remove(messageId);
    }

    public static void removeRsp(RpcResponse response) {
        receiveList.remove(response.getMessageId());
    }

    public static boolean isContains(RpcResponse response) {
        return receiveList.containsKey(response.getMessageId());
    }
}

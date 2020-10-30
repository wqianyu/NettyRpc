package com.netty.pool6.client;

import com.netty.pool6.vo.RpcRequest;
import com.netty.pool6.vo.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/20.
 */
@Component
public class NettyClient {

    @Autowired
    private NettyClientProxy proxy;

    private final Integer maxTimeout = 10;

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private static ConcurrentHashMap<String, SyncFuture<RpcResponse>> receiveList = new ConcurrentHashMap<>();

    public static void removeRsp(RpcResponse response) {
        receiveList.remove(response.getMessageId());
    }

    public static boolean isContains(RpcResponse response) {
        return receiveList.containsKey(response.getMessageId());
    }

    public RpcResponse send(InetSocketAddress address, RpcRequest request) {

        RpcResponse response = null;
        try {
            Channel channel = proxy.getClient(address);//通过address获取连接池连接
            SyncFuture<RpcResponse> future = new SyncFuture<>();//新建请求实例
            receiveList.put(request.getMessageId(), future);//放到队列
            ChannelFuture channelFuture = channel.writeAndFlush(request);//发送消息
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    LOGGER.info("async result :"+future.get());
                }
            });
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
            if (null != response && isContains(response)) {//清理过期/已接收的数据，防止队列过大
                removeRsp(response);
            }
            LOGGER.info("_________active_______"+size());
        }
        return response;
    }

    public static void callNotify(String messageId, RpcResponse response) {//接收消息线程唤醒处理线程
        SyncFuture<RpcResponse> future = receiveList.get(messageId);
        if (null != future) {
            LOGGER.info("_________notify_______");
            future.setResponse(response);
        } else {
            LOGGER.info("_________no found_______");
        }
    }

    public static int size() {
        return receiveList.size();
    }
}

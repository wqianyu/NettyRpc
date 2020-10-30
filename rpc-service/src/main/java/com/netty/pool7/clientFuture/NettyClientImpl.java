package com.netty.pool7.clientFuture;

import com.netty.pool7.client.ASyncFuture;
import com.netty.pool7.client.NettyClientProxy;
import com.netty.pool7.client.SyncFuture;
import com.netty.pool7.listener.GenericFutureListener;
import com.netty.pool7.listener.future.RpcFuture;
import com.netty.pool7.vo.RpcRequest;
import com.netty.pool7.vo.RpcResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Administrator on 2018/9/7.
 */
public class NettyClientImpl implements NettyClientInterface<RpcResponse> {

    private final Integer maxTimeout = 10;

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientImpl.class);

    private static ConcurrentHashMap<String, SyncFuture<RpcResponse>> receiveList = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ASyncFuture<RpcResponse>> asyncList = new ConcurrentHashMap<>();

    private NettyClientProxy proxy = NettyClientProxy.getProxy().initMaxSize(2);//通过address获取连接池连接

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    @Override
    public RpcResponse send(InetSocketAddress address, RpcRequest request) {
        return null;
    }

    @Override
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
            removeRsp(response);
            future.setResponse(response);
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

    @Override
    public void addListener(GenericFutureListener<? extends RpcFuture<? super RpcResponse>> var1) {
    }
}

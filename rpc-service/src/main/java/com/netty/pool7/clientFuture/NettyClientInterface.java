package com.netty.pool7.clientFuture;

import com.netty.pool7.client.SyncFuture;
import com.netty.pool7.listener.GenericFutureListener;
import com.netty.pool7.listener.future.RpcFuture;
import com.netty.pool7.vo.RpcRequest;
import com.netty.pool7.vo.RpcResponse;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/9/7.
 */
public interface NettyClientInterface<V> {
    public RpcResponse send(InetSocketAddress address, RpcRequest request);

    public SyncFuture sendAsync(InetSocketAddress address, RpcRequest request);

    void addListener(GenericFutureListener<? extends RpcFuture<? super V>> var1);
}

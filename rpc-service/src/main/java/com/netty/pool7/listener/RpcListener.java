package com.netty.pool7.listener;

import com.netty.pool7.listener.future.RpcFuture;

/**
 * Created by Administrator on 2018/9/1.
 */
public interface RpcListener<V> extends GenericFutureListener<RpcFuture<V>> {
}

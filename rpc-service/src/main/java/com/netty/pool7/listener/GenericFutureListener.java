package com.netty.pool7.listener;


import com.netty.pool7.client.ASyncFuture;
import com.netty.pool7.listener.future.RpcFuture;
import com.netty.pool7.vo.RpcResponse;

import java.util.EventListener;

/**
 * Created by Administrator on 2018/9/1.
 */
public interface GenericFutureListener<F extends RpcFuture<?>> extends EventListener {
    void operationComplete(ASyncFuture<RpcResponse> var1) throws Exception;
}

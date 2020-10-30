package com.netty.pool3.countDownClient;

import com.netty.pool3.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/7/17.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    static AtomicInteger count = new AtomicInteger(1);

    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse response = (RpcResponse) msg;
        System.out.println(count.getAndIncrement() + ":" + response);
        scheduledExecutorService.execute(new ReceiveThread(ctx.channel(), response));
    }
}
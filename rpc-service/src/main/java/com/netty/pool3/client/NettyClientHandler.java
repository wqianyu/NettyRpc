package com.netty.pool3.client;

import com.netty.pool3.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/7/17.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse response = (RpcResponse) msg;
        System.out.println(count.getAndIncrement() + ":" + response);
        if (NettyClient.islock(response.getMessageId())) {//判断对象锁是否还存在（超时是会被移除）丢弃结果
            NettyClient.addResponse(response);//未超时，添加到消息队列，唤醒阻塞线程处理
            NettyClient.callNotify(response.getMessageId());
        }
    }
}
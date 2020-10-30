package com.netty.pool5.client;

import com.netty.pool5.vo.MsgType;
import com.netty.pool5.vo.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/7/17.
 */
//@Component

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse response = (RpcResponse) msg;
        System.out.println(count.getAndIncrement() + ":" + response);
        if(response.getMsgType() == MsgType.MSG) {
            if (NettyClient.islock(response.getMessageId())) {//判断对象锁是否还存在（超时是会被移除）丢弃结果
                NettyClient.addResponse(response);//未超时，添加到消息队列，唤醒阻塞线程处理
                NettyClient.callNotify(response.getMessageId());
            }
        } else {
            System.out.println("---- heartBeat:"+response.toString());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("=============channelInactive===============");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("=======exceptionCaught======");
    }
}
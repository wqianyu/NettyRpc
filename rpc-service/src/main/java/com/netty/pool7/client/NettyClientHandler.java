package com.netty.pool7.client;

import com.netty.pool7.vo.MsgType;
import com.netty.pool7.vo.RpcResponse;
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
            NettyClient.callNotify(response.getMessageId(), response);//接收到服务器返回值，唤醒客户端future任务
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
package com.clientpool.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2018/7/10.
 */
public class KyroClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * Creates a client-side handler.
     */
    public KyroClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // you can use the Object from Server here
        System.out.println("client receive msg:"+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
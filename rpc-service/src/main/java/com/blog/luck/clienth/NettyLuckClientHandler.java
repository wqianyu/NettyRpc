package com.blog.luck.clienth;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/9.
 */
public class NettyLuckClientHandler extends SimpleChannelInboundHandler<LuckMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LuckMessage message) throws Exception {
        System.out.println(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("=== client channelRead ===");
        // 简单地打印出server接收到的消息
        System.out.println(msg.toString());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("=== client active ===");
        int version = 1;
        String sessionId = UUID.randomUUID().toString();
        String content = "I'm the luck protocol!";

        LuckHeader header = new LuckHeader(version, content.length(), sessionId);
        LuckMessage message = new LuckMessage(header, content);
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

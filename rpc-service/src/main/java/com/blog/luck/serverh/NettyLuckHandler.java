package com.blog.luck.serverh;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/9.
 */
public class NettyLuckHandler extends SimpleChannelInboundHandler<LuckMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LuckMessage msg) throws Exception {
        // 简单地打印出server接收到的消息
        System.out.println(msg.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Thread.sleep(500);
        // 简单地打印出server接收到的消息
        System.out.println(msg.toString());
        int version = 1;
        String sessionId = UUID.randomUUID().toString();
        String content = "had received!";

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

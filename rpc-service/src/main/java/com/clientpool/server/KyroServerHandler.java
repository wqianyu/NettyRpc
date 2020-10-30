package com.clientpool.server;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/10.
 */
public class KyroServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        System.out.println("server receive msg:"+msg);

        int version = ((LuckMessage)msg).getLuckHeader().getVersion();
        String sessionId = UUID.randomUUID().toString();
        String content = "had received!";

        LuckHeader header = new LuckHeader(version, content.length(), sessionId);
        LuckMessage message = new LuckMessage(header, content);

        //System.out.println("server write msg:"+message);
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

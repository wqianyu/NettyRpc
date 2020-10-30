package com.blog.luck.clienth;

import com.blog.luck.common.RpcRequest;
import com.blog.luck.common.RpcResponse;
import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/9.
 */
public class RequestHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse message) throws Exception {
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
        RpcRequest data = new RpcRequest();
        data.setIntefId(1);
        data.setSid(1);
        /*Map<String, String> map = new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        data.setObject(JSONObject.toJSON(map));*/
        int version = 1;
        String sessionId = UUID.randomUUID().toString();
        String content = "I'm the luck protocol!";

        LuckHeader header = new LuckHeader(version, content.length(), sessionId);
        LuckMessage message = new LuckMessage(header, content);
        data.setObject(message);
        ctx.writeAndFlush(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

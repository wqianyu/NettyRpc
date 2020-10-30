package com.blog.luck.serverh;

import com.alibaba.fastjson.JSONObject;
import com.blog.luck.common.RpcRequest;
import com.blog.luck.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/7/13.
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        // 简单地打印出server接收到的消息
        System.out.println(msg.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 简单地打印出server接收到的消息
        System.out.println(msg.toString());
        int version = 1;
        String sessionId = UUID.randomUUID().toString();
        String content = "had received!";

        RpcResponse data = new RpcResponse();
        data.setStatusCode(200);
        Map<String, String> map = new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        data.setResponse(JSONObject.toJSON(map));
        ctx.writeAndFlush(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

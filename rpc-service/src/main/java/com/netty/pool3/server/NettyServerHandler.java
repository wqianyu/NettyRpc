package com.netty.pool3.server;

import com.alibaba.fastjson.JSONObject;
import com.netty.pool3.common.RpcRequest;
import com.netty.pool3.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/7/17.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    static AtomicInteger count = new AtomicInteger(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channelActived :" + ctx.channel().id());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest)msg;
        int num = count.getAndIncrement();
        LOGGER.info(ctx.channel().id()+","+num + ":" + msg);
        RpcResponse response = new RpcResponse();
        response.setStatusCode(200);
        JSONObject json = new JSONObject();
        json.put("msg", "");
        json.put("num", num);
        json.put("code", 0);
        json.put("data", request);
        response.setMessageId(request.getMessageId());
        response.setResponse(json);
        //Thread.sleep(10000);//模拟服务器端超时场景，客户端超时异常，丢弃结果
        ctx.writeAndFlush(response);
    }
}
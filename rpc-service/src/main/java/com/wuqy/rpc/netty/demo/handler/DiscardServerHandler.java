package com.wuqy.rpc.netty.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/7/6.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(DiscardServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        //((ByteBuf) msg).release(); // (3)不操作直接丢弃接收的内容

        ByteBuf in = (ByteBuf) msg;
        try {
            //while (in.isReadable()) { // (1)接收处理数据，按字节
            //    System.out.print((char) in.readByte());//转化为字符
            //    System.out.flush();
            //}
            System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));//转化为字符串
        } finally {
            //ReferenceCountUtil.release(msg); // (2)丢弃消息，也可以用in.release(),如果还要用，不能丢弃
        }
        //ctx.write(msg);//只是写到缓冲区
        //ctx.flush();//清空缓冲区，写入response
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("--- receive an active ---");
        //super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("--- an active failed ---");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}

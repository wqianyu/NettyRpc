package com.wuqy.rpc.netty.demo.clienthandler;

import com.wuqy.rpc.netty.demo.common.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/6.
 */
public class TimeUnixClientHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeUnixClientHandler.class);

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {//解决传过来的日期字节数据分片问题
        buf = ctx.alloc().buffer(4); // (1)
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release(); // (1)
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LOGGER.info("=== channelRead ===");
        if(msg instanceof UnixTime) {
            UnixTime m = (UnixTime) msg;
            LOGGER.info(m.toString());
            ctx.close();
        }

        if (buf.readableBytes() >= 4) { // (3)
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

package com.wuqy.rpc.netty.demo.handler;

import com.wuqy.rpc.netty.demo.common.UnixTime;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/7/6.
 */
public class TimeUnitServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeUnitServerHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("--- an active failed ---");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        LOGGER.info("--- receive an active ---");
        try {
            ChannelFuture f = ctx.writeAndFlush(new UnixTime());//自动调用TimeEncoder加密数据
            f.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            LOGGER.warn("channelActive error", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

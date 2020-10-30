package com.clientpool.pool;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/7/17.
 */
public class CloseFutureListener implements ChannelFutureListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(CloseFutureListener.class);

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        LOGGER.info("operationComplete");
        System.out.println("operationComplete");
        channelFuture.channel().eventLoop().shutdownGracefully();
    }
}

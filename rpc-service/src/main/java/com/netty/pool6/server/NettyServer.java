package com.netty.pool6.server;

import com.netty.pool6.encode.RequestDecoder;
import com.netty.pool6.encode.ResponseEncoder;
import com.netty.pool6.trigger.AcceptorIdleStateTrigger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/17.
 */
public class NettyServer {
    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    public void run(final int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RequestDecoder())
                                    .addLast(new ResponseEncoder())
                                    .addLast(new DefaultEventExecutorGroup(8),new NettyServerHandler())
                                    .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                                    .addLast(idleStateTrigger);
                                    /*.addLast(new DefaultEventExecutorGroup(8),
                                            new NettyServerHandler());*/
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        new NettyServer().run(port);
    }
}

package com.wuqy.rpc.netty.demo.server;

import com.wuqy.rpc.netty.demo.encode.TimeEncoder2;
import com.wuqy.rpc.netty.demo.handler.TimeUnitServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Administrator on 2018/7/6.
 */
public class TimeUnitServer {

    private int port;

    public TimeUnitServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)接收连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理连接，并注册到worker
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)一个server创建帮助类，去创建一个server；也可以直接用channel去创建
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)NioServerSocketChannel用于初始化一个新的channel去接收连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)创建一个或多个handler去操作连接，该例子是DiscardServerHandler，可根据需要添加
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeEncoder2(), new TimeUnitServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5) channel通道的扩展参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)启动并绑定端口，可以多次绑定面向不同接口

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new TimeUnitServer(port).run();
    }
}

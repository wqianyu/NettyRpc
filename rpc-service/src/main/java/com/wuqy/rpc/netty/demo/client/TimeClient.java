package com.wuqy.rpc.netty.demo.client;


import com.wuqy.rpc.netty.demo.clienthandler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2018/7/6.
 */
public class TimeClient {

    public static final String host = "127.0.0.1";

    public static final int port = 8080;

    public static void main(String[] args) throws Exception {
        String host = TimeClient.host;
        int port = TimeClient.port;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)与服务器端ServerBootstrap类似，用于建立客户端连接通道
            b.group(workerGroup); // (2)如果指定了EventLoopGroup，它既是boss group也是worker group。boss group在客户端没有用处
            b.channel(NioSocketChannel.class); // (3)NioSocketChannel用于奖励客户端通道（服务器端是：NioServerSocketChannel）
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)没有childOption，因为客户端的socketChannel没有parent
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)客户端是connect，服务器端是bind

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

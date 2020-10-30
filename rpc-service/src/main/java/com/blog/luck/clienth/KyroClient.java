package com.blog.luck.clienth;

import com.blog.luck.endecode.KyroDecoder;
import com.blog.luck.endecode.KyroEncoder;
import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/10.
 */
public class KyroClient {

    private String host;
    private int port;
    private LuckMessage message;

    public KyroClient(String host, int port, LuckMessage message) {
        this.host = host;
        this.port = port;
        this.message = message;
    }

    public void send() throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new KyroEncoder(),
                                    new KyroDecoder(),
                                    new KyroClientHandler(message));
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        int version = 1;
        String sessionId = UUID.randomUUID().toString();
        String content = "had received!";

        LuckHeader header = new LuckHeader(version, content.length(), sessionId);
        LuckMessage message = new LuckMessage(header, content);

        new KyroClient("127.0.0.1", 8081, message).send();
    }
}

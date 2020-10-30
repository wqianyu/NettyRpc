package com.clientpool.pool;

import com.clientpool.client.KyroClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/7/16.
 */
public class NettyClientFactory extends BasePoolableObjectFactory<Channel> {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientFactory.class);

    @Override
    public boolean validateObject(Channel obj) {
        boolean state = obj.isActive();
        LOGGER.info("NettyClientFactory validate:"+(state?"校验通过":"校验不通过"));
        return state;
    }

    @Override
    public void activateObject(Channel obj) throws Exception {
        LOGGER.info("=== activate ===");
    }

    @Override
    public void passivateObject(Channel obj) throws Exception {
        LOGGER.info("=== passivate ===");
    }

    @Override
    public void destroyObject(Channel obj) throws Exception {
        LOGGER.info("=== destroy ===");
        //http://netty.io/4.0/api/io/netty/channel/Channel.html
        obj.closeFuture().sync();//不能直接使用close或直接关闭，会导致正在传输数据丢失。下面语句有问题

        /*if (!obj.isActive()) {
            LOGGER.info("=== destroy close ===");
            obj.close();
            obj.eventLoop().shutdownGracefully();
        }*/

        //ChannelFuture f =obj.closeFuture();
        //f.addListener(future -> System.out.println("success complete!!ok!!"));

        LOGGER.info("=== destroy finish ===");
    }

    @Override
    public Channel makeObject() throws Exception {
        LOGGER.info("=== makeObject ===");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //.handler(new LuckClientInitializer());
                    .handler(new KyroClientInitializer());
            Channel channel = b.connect("127.0.0.1", 8888).sync().channel(); // (5)客户端是connect，服务器端是bind
            //channel.closeFuture().sync();
            return channel;
        } finally {
            //group.shutdownGracefully();
        }
    }
}

package com.netty.pool3.countDownClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/7/19.
 */
public class NettyClientProxy {

    private final int maxSize = 50;

    final EventLoopGroup group = new NioEventLoopGroup();

    final Bootstrap strap = new Bootstrap();

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientProxy.class);

    private SimpleChannelPool pool;

    private ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    private Class<?> objectClass;

    private Object proxyClient;

    {
        strap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                LOGGER.info("newPool =======");
                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(), maxSize);
            }
        };
    }

    public Channel getClient(InetSocketAddress address) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        objectClass = classLoader.loadClass(Channel.class.getName());
        if (null == poolMap.get(address)) {
            return null;
        }
        proxyClient = Proxy.newProxyInstance(classLoader, new Class[] { objectClass }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Channel client = null;
                try {
                    LOGGER.info("before get =======");
                    pool = poolMap.get(address);
                    Future<Channel> f = pool.acquire();
                    f.await();//阻塞，等待pool获取成功过
                    client = f.getNow();
                    if (null == client) {
                        throw new Exception("获取连接失败");
                    }
                    LOGGER.info("after get =======");
                    return method.invoke(client, args);
                } finally {
                    LOGGER.info("finally =======");
                    if (null != client) {
                        pool.release(client);
                    }
                }
            }
        });
        return (Channel)proxyClient;
    }
}

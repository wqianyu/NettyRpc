package com.netty.pool7.client;

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
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/19.
 */
public class NettyClientProxy {

    private int maxSize = 5;

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientProxy.class);

    private ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    public NettyClientProxy initMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    private NettyClientProxy() {

    }

    public Channel build(InetSocketAddress address) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> objectClass = classLoader.loadClass(Channel.class.getName());
        Object proxyClient = Proxy.newProxyInstance(classLoader, new Class[] { objectClass }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Channel client = null;
                SimpleChannelPool pool = null;
                try {
                    pool = poolMap.get(address);//获取池
                    Future<Channel> f = pool.acquire();//获取连接
                    f.await(1000, TimeUnit.MILLISECONDS);//阻塞，等待pool获取成功过
                    client = f.getNow();
                    if(null == client) {
                        throw new Exception("获取client连接失败");
                    }
                    return method.invoke(client, args);
                } catch (Exception e) {
                    LOGGER.warn("获取连接失败", e);
                    throw e;
                } finally {
                    if (null != client) {
                        pool.release(client);
                    }
                }
            }
        });
        return (Channel)proxyClient;
    }

    public static NettyClientProxy getProxy() {
        NettyClientProxy proxy = new NettyClientProxy();
        HashedWheelTimer timer = new HashedWheelTimer();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap strap = new Bootstrap();
        strap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        //初始化netty自带连接池实现工具，以InetSocketAddress为key，channelPool为返回值
        proxy.poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                LOGGER.info("newPool =======");
                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(strap, timer, key.getPort(), key.getHostName(), true ), proxy.maxSize);
            }
        };
        return proxy;
    }
}

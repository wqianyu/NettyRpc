package com.netty.pool5.client;

import com.netty.pool5.trigger.ConnectorIdleStateTrigger;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/19.
 */
@Component
public class NettyClientProxy implements InitializingBean{

    private final int maxSize = 5;

    final Bootstrap strap = new Bootstrap();

    final EventLoopGroup group = new NioEventLoopGroup();

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientProxy.class);

    private SimpleChannelPool pool;

    private ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    private Class<?> objectClass;

    private Object proxyClient;

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    private Channel channel = null;

    public Channel getClient(InetSocketAddress address) throws Exception {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        objectClass = classLoader.loadClass(Channel.class.getName());
        proxyClient = Proxy.newProxyInstance(classLoader, new Class[] { objectClass }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Channel client = null;
                try {
                    //LOGGER.info("before get =======");
                    pool = poolMap.get(address);
                    Future<Channel> f = pool.acquire();
                    f.await(1000, TimeUnit.MILLISECONDS);//阻塞，等待pool获取成功过
                    client = f.getNow();
                    if(null == client) {
                        throw new Exception("获取client连接失败");
                    }
                    //LOGGER.info("after get =======");
                    return method.invoke(client, args);
                } catch (Exception e) {
                    LOGGER.warn("获取连接失败", e);
                    throw e;
                    //return null;
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

    protected final HashedWheelTimer timer = new HashedWheelTimer();

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        strap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                LOGGER.info("newPool =======");
                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(strap, timer, key.getPort(), key.getHostName(), true ), maxSize);
            }
        };
    }
}

package com.netty.pool2.client;

import com.google.common.reflect.Reflection;
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
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/7/19.
 */
public class NettyClientProxy {

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
                return new FixedChannelPool(strap.remoteAddress(key), new com.netty.pool.client.NettyChannelPoolHandler(), 2);
            }
        };
    }

    public Channel getClient(InetSocketAddress address) throws Exception {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        objectClass = classLoader.loadClass(Channel.class.getName());
//        proxyClient = Proxy.newProxyInstance(classLoader, new Class[] { objectClass }, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                Channel client = null;
//                try {
//                    LOGGER.info("before get =======");
//                    pool = poolMap.get(address);
//                    Future<Channel> f = pool.acquire();
//                    f.await();//阻塞，等待pool获取成功过
//                    client = f.getNow();
//                    LOGGER.info("after get =======");
//                    return method.invoke(client, args);
//                } catch (Exception e) {
//                    LOGGER.warn("获取连接失败", e);
//                    return null;
//                } finally {
//                    LOGGER.info("finally =======");
//                    if (null != client) {
//                        pool.release(client);
//                    }
//                }
//            }
//        });
        proxyClient = Reflection.newProxy(Channel.class,  new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Channel client = null;
                try {
                    LOGGER.info("before get =======");
                    pool = poolMap.get(address);
                    Future<Channel> f = pool.acquire();
                    f.await(3000);//阻塞，等待pool获取成功过
                    client = f.getNow();
                    LOGGER.info("after get =======");
                    return method.invoke(client, args);
                } catch (Exception e) {
                    LOGGER.warn("获取连接失败", e);
                    return null;
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

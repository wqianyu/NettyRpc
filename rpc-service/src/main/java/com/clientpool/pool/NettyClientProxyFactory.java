package com.clientpool.pool;

import io.netty.channel.Channel;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2018/7/16.
 */
public class NettyClientProxyFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientFactory.class);

    private Integer maxActive = 5;// 最大活跃连接数.多client高并发时，数据会丢失。一个server进程，并发流串混淆（自己做的简易版粘包拆包有该问题）

    // ms,default 3 min,链接空闲时间
    // -1,关闭空闲检测
    private Integer idleTime = 180000;

    private Object proxyClient;
    private Class<?> objectClass;

    private GenericObjectPool<Channel> pool;

    public Channel getPoolClient() throws Exception {
        if (pool == null) {
            init();
        }
        return pool.borrowObject();
    }
    public Channel getClient() {
        try {
            if (null == proxyClient) {
                init();
            }
            return (Channel)proxyClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void init() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        objectClass = classLoader.loadClass(Channel.class.getName());
        NettyClientFactory clientFactory = new NettyClientFactory();
        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        poolConfig.maxActive = maxActive;
        poolConfig.maxIdle = maxActive-1;//1;//maxActive+1;//1;
        poolConfig.minIdle = 0;
        poolConfig.minEvictableIdleTimeMillis = idleTime;
        poolConfig.timeBetweenEvictionRunsMillis = idleTime * 2L;
        poolConfig.testOnBorrow=true;
        poolConfig.testOnReturn=true;
        poolConfig.testWhileIdle=true;
        pool = new GenericObjectPool<Channel>(clientFactory, poolConfig);
        proxyClient = Proxy.newProxyInstance(classLoader, new Class[] { objectClass }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Channel client = pool.borrowObject();//当空闲池不足以支撑并发量时，会再次调用makeObject生成连接
                boolean flag = true;
                try {
                    LOGGER.info("~~~~~~~~active:"+pool.getNumActive()+",idle:"+pool.getNumIdle()+"~~~~~~~~");
                    //Thread.sleep(500);
                    return method.invoke(client, args);
                } catch (Exception e) {
                    flag = false;
                    LOGGER.warn("获取连接失败", e);
                    return null;
                } finally {
                    if(flag){
                        LOGGER.info("*** return ***");
                        pool.returnObject(client);
                    }else{
                        LOGGER.info("*** invalidate ***");
                        pool.invalidateObject(client);
                    }
                }
            }
        });
    }

    public Integer getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Object getObject() throws Exception {
        return proxyClient;
    }

    public Class<?> getObjectType() {
        return objectClass;
    }

    public boolean isSingleton() {
        return true;
    }

    public void close() {
        if(pool!=null){
            try {
                pool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

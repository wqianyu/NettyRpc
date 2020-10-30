package com.netty.pool2.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/7/19.
 * 使用netty自带的连接池，实现客户端高并发
 */
public class Client {
    public static void main(String[] args) {
        try {
            NettyClientProxy proxy = new NettyClientProxy();
            InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8888);
            Channel client = proxy.getClient(addr1);
            for (int i = 0; i < 10; i++) {
                String ECHO_REQ = "Hello Netty.";
                ECHO_REQ += ""+i;
                ECHO_REQ += "$_";
                client.writeAndFlush(ECHO_REQ);
            }
            Thread.sleep(3000);
            InetSocketAddress addr2 = new InetSocketAddress("127.0.0.1", 8889);
            client = proxy.getClient(addr2);
            for (int i = 10; i < 20; i++) {
                String ECHO_REQ = "Hello Netty.";
                ECHO_REQ += ""+i;
                ECHO_REQ += "$_";
                client.writeAndFlush(ECHO_REQ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

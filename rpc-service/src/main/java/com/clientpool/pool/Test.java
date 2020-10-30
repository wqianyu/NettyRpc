package com.clientpool.pool;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.channel.Channel;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/16.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        NettyClientProxyFactory proxy = new NettyClientProxyFactory();
        Integer length = 100;
        Channel channel;
        int version = 1;
        String sessionId = UUID.randomUUID().toString();
        String content = "I'm the luck protocol!";

        channel = proxy.getClient();
        for (int i = 0; i < length; i++){
            LuckHeader header = new LuckHeader(version, content.length(), sessionId);
            LuckMessage message = new LuckMessage(header, content);
            header.setVersion(i);
            channel.writeAndFlush(message);
        }
        System.out.println("----finish----");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("----close----");
        channel.close();
        //channel.eventLoop().shutdownGracefully();

        /*LuckHeader header = new LuckHeader(version, content.length(), sessionId);
        LuckMessage message = new LuckMessage(header, content);
        header.setVersion(5);
        channel.writeAndFlush(message);*/
    }
}

package com.clientpool.pool;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.channel.Channel;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/16.
 * 使用apache common pools实现netty客户端连接池
 */
public class PoolTest {
    public static void main(String[] args) throws Exception {
        NettyClientProxyFactory proxy = new NettyClientProxyFactory();
        Integer length = 50;
        Channel channel;

        String sessionId = UUID.randomUUID().toString();
        String content = "I'm the luck protocol!dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶多多多多多多顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶";
        channel = proxy.getClient();//proxy.getPoolClient();
        for (int i = 0; i < length; i++){
            int version = i;
            new Thread(){
                @Override
                public void run() {
                        LuckHeader header = new LuckHeader(version, content.length(), sessionId);
                        LuckMessage message = new LuckMessage(header, content);
                        header.setVersion(version);
                        channel.writeAndFlush(message);
                    }
            }.start();
        }
        System.out.println("----finish----");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("----close----");
        //channel.close();
        //channel.eventLoop().shutdownGracefully();

        LuckHeader header = new LuckHeader(999, content.length(), sessionId);
        LuckMessage message = new LuckMessage(header, content);
        channel.writeAndFlush(message);
    }
}

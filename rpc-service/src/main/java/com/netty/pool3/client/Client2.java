package com.netty.pool3.client;

import com.alibaba.fastjson.JSONObject;
import com.netty.pool3.common.RpcRequest;
import com.netty.pool3.common.RpcResponse;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/7/19.
 * 使用netty自带的连接池，实现客户端高并发
 */
public class Client2 {
    public static void main(String[] args) {
        try {
            NettyClientProxy proxy = new NettyClientProxy();
            InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8888);
            Channel client = proxy.getClient(addr1);
            for(int count = 0; count < 1; count++) {
                for (int i = 0; i < 10; i++) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                NettyClient nettyClient = new NettyClient();
                                RpcRequest req = new RpcRequest();
                                System.out.println(Math.random());
                                req.setSid((int) (Math.random() * 10));
                                req.setIntefId(req.getSid() * 10 + 1);
                                JSONObject json = new JSONObject();
                                json.put("testObject", "data:" + req.getSid());
                                req.setObject(json);
                                long begin = System.currentTimeMillis();
                                RpcResponse response = nettyClient.send(client, req);
                                System.out.println("~~~~~~~~~~client response:" + response + ",time:" + (System.currentTimeMillis() - begin));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                //Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

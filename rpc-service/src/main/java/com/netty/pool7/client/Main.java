package com.netty.pool7.client;

import com.alibaba.fastjson.JSONObject;
import com.netty.pool7.vo.RpcRequest;
import com.netty.pool7.vo.RpcResponse;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/8/31.
 */
public class Main {

    static NettyClient client = new NettyClient();

    public static void main(String[] args) {
        InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8888);
        for(int i = 0; i < 50; i++) {
            RpcRequest req = new RpcRequest();
            System.out.println(Math.random());
            req.setSid((int) (Math.random() * 10));
            req.setIntefId(req.getSid() * 10 + 1);
            JSONObject json = new JSONObject();
            json.put("testObject", "data:" + req.getSid());
            req.setObject(json);
            long begin = System.currentTimeMillis();
            RpcResponse response = client.send(addr1, req);
            System.out.println("消耗时间：" + (System.currentTimeMillis() - begin) + " resp:"+response.toString());

            SyncFuture future = client.sendAsync(addr1, req);
            try {
                System.out.println("消耗时间：" + (System.currentTimeMillis() - begin) + " resp:" + future.get().toString());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

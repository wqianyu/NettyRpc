package com.test.wuqy.action;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/5/12.
 */
@RestController
public class TestController {
//
//    private NettyClientProxy proxy = new NettyClientProxy();
//
//    @GetMapping("/test/{id}")
//    public RpcResponse test() throws Exception {
//        InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8888);
//        Channel client = proxy.getClient(addr1);
//        NettyClient nettyClient = new NettyClient();
//        RpcRequest req = new RpcRequest();
//        System.out.println(Math.random());
//        req.setSid((int) (Math.random() * 10));
//        req.setIntefId(req.getSid() * 10 + 1);
//        JSONObject json = new JSONObject();
//        json.put("testObject", "data:" + req.getSid());
//        req.setObject(json);
//        long begin = System.currentTimeMillis();
//        RpcResponse response = null;
//        if (null != client) {
//            response = nettyClient.send(client, req);
//        } else {
//            System.out.println("获取不到连接client");
//        }
//        System.out.println("消耗时间："+(System.currentTimeMillis()-begin));
//        return response;
//    }

    @GetMapping("/index")
    public String index() throws Exception {
        return "index.jsp";
    }
}

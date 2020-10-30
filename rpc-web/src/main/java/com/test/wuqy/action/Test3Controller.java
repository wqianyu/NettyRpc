package com.test.wuqy.action;

import com.alibaba.fastjson.JSONObject;
import com.netty.pool6.client.NettyClient;
import com.netty.pool6.vo.RpcRequest;
import com.netty.pool6.vo.RpcResponse;
import com.yy.platform.common.model.wonderful.NewNavSubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import test.TestService;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@RestController
public class Test3Controller {

    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private TestService testService;

    @GetMapping("/newtest")
    public RpcResponse newtest() throws Exception {
        InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8888);
        RpcRequest req = new RpcRequest();
        System.out.println(Math.random());
        req.setSid((int) (Math.random() * 10));
        req.setIntefId(req.getSid() * 10 + 1);
        JSONObject json = new JSONObject();
        json.put("testObject", "data:" + req.getSid());
        req.setObject(json);
        long begin = System.currentTimeMillis();
        RpcResponse response = nettyClient.send(addr1, req);
        System.out.println("消耗时间："+(System.currentTimeMillis()-begin));
        return response;
    }

    @GetMapping("/demo")
    public JSONObject demo() throws Exception {
        JSONObject object = new JSONObject();
        List<NewNavSubscribe> data = testService.getAll();
        object.put("data", data);
        return object;
    }
}

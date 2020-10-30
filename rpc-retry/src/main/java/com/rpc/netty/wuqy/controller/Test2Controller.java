package com.rpc.netty.wuqy.controller;

import com.rpc.netty.wuqy.annotation.AnnoTationTest;
import com.rpc.netty.wuqy.example_annotation.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/5/12.
 */
@RestController
public class Test2Controller {

    @Autowired
    private AnnoTationTest annoTationTest;

    @GetMapping("/test2")
    public String test2() throws Exception {
        annoTationTest.fiveTimes();
        return "test";
    }

    @Autowired
    private RemoteService remoteService;

    @GetMapping("/testRemote")
    public String testRemote() throws Exception {
        remoteService.call();
        return "test";
    }
}

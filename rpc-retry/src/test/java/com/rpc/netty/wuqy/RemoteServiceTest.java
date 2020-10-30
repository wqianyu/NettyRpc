package com.rpc.netty.wuqy;

import com.rpc.netty.wuqy.example_annotation.RemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RemoteServiceTest {

    @Autowired
    private RemoteService remoteService;

    @Test
    public void test() {
        remoteService.call();
    }

}

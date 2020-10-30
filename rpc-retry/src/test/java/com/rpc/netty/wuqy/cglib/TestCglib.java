package com.rpc.netty.wuqy.cglib;

import com.rpc.netty.wuqy.example_cglib.CglibProxy;
import com.rpc.netty.wuqy.service.UserService;
import com.rpc.netty.wuqy.service.impl.UserServiceImpl;
import com.rpc.netty.wuqy.vo.QueryUserCondition;
import com.rpc.netty.wuqy.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TestCglib {

    @Test
    public void failUserServiceTest() {
        UserService proxyService = (UserService) new CglibProxy().getProxy(UserServiceImpl.class);

        User user = proxyService.queryUser(new QueryUserCondition());
        log.info("failUserServiceTest: " + user);
    }

    @Test
    public void resourceServiceTest() {
//        ResourceServiceImpl proxyService = (ResourceServiceImpl) new CglibProxy().getProxy(ResourceServiceImpl.class);
//        boolean result = proxyService.checkResource(new User());
//        LOGGER.info("resourceServiceTest: " + result);
    }

}

package com.rpc.netty.wuqy.dynamic;

import com.rpc.netty.wuqy.example_dynamic_proxy.DynamicProxy;
import com.rpc.netty.wuqy.service.UserService;
import com.rpc.netty.wuqy.service.impl.UserServiceImpl;
import com.rpc.netty.wuqy.vo.QueryUserCondition;
import com.rpc.netty.wuqy.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TestDynamic {

    @Test
    public void failUserServiceTest() {
        UserService realService = new UserServiceImpl();
        UserService proxyService = (UserService) DynamicProxy.getProxy(realService);

        User user = proxyService.queryUser(new QueryUserCondition());
        log.info("failUserServiceTest: " + user);
    }


    @Test
    public void roleServiceTest() {
//        RoleService realService = new RoleServiceImpl();
//        RoleService proxyService = (RoleService) DynamicProxy.getProxy(realService);
//
//        boolean hasPrivilege = proxyService.hasPrivilege(new User());
//        log.info("roleServiceTest: " + hasPrivilege);
    }
}

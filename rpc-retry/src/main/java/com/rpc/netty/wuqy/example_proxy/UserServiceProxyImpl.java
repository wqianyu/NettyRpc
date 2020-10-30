package com.rpc.netty.wuqy.example_proxy;

import com.rpc.netty.wuqy.constant.RetryConstant;
import com.rpc.netty.wuqy.service.UserService;
import com.rpc.netty.wuqy.service.impl.UserServiceImpl;
import com.rpc.netty.wuqy.vo.QueryUserCondition;
import com.rpc.netty.wuqy.vo.User;

public class UserServiceProxyImpl implements UserService {

    private UserService userService = new UserServiceImpl();

    @Override
    public User queryUser(QueryUserCondition condition) {
        int times = 0;

        while (times < RetryConstant.MAX_TIMES) {
            try {
                return userService.queryUser(condition);
            } catch (Exception e) {
                times++;

                if(times >= RetryConstant.MAX_TIMES) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

}

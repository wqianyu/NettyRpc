package com.rpc.netty.wuqy.example;

import com.rpc.netty.wuqy.constant.RetryConstant;
import com.rpc.netty.wuqy.service.OutService;
import com.rpc.netty.wuqy.service.UserService;
import com.rpc.netty.wuqy.service.impl.AlwaysFailOutServiceImpl;
import com.rpc.netty.wuqy.vo.QueryUserCondition;
import com.rpc.netty.wuqy.vo.User;

public class UserServiceRetryImpl implements UserService {

    @Override
    public User queryUser(QueryUserCondition condition) {
        int times = 0;
        OutService outService = new AlwaysFailOutServiceImpl();

        while (times < RetryConstant.MAX_TIMES) {
            try {
                outService.remoteCall();
                return new User();
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

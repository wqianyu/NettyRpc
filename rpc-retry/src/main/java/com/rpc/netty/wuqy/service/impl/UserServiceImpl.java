package com.rpc.netty.wuqy.service.impl;

import com.rpc.netty.wuqy.service.OutService;
import com.rpc.netty.wuqy.service.UserService;
import com.rpc.netty.wuqy.vo.QueryUserCondition;
import com.rpc.netty.wuqy.vo.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserServiceImpl implements UserService {

    private OutService outService;

    public UserServiceImpl(OutService outService) {
        this.outService = outService;
    }

    @Override
    public User queryUser(QueryUserCondition condition) {
        outService.remoteCall();
        return new User();
    }

}

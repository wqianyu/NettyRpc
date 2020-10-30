package com.rpc.netty.wuqy.service;

import com.rpc.netty.wuqy.vo.QueryUserCondition;
import com.rpc.netty.wuqy.vo.User;

public interface UserService {

    /**
     * 根据条件查询用户信息
     * @param condition 条件
     * @return User 信息
     */
    User queryUser(QueryUserCondition condition);

}

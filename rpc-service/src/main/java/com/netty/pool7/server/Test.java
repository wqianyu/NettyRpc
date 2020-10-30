package com.netty.pool7.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/8/1.
 */
@Component
public class Test implements InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(Test.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("=========== init ===========");
    }
}

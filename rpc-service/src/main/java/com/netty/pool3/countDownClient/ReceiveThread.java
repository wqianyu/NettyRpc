package com.netty.pool3.countDownClient;

import com.netty.pool3.common.RpcResponse;
import io.netty.channel.Channel;

/**
 * Created by Administrator on 2018/7/24.
 */
public class ReceiveThread extends Thread {

    private RpcResponse response;

    private Channel channel;

    public ReceiveThread(Channel channel, RpcResponse response) {
        this.channel = channel;
        this.response = response;
    }

    @Override
    public void run() {
        if (NettyClient.islock(response.getMessageId())) {//判断对象锁是否还存在（超时是会被移除）丢弃结果
            NettyClient.addResponse(response);//未超时，添加到消息队列，唤醒阻塞线程处理
            NettyClient.callNotify(response.getMessageId());
        }
    }
}

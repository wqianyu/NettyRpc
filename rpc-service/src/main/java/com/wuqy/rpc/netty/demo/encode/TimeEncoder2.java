package com.wuqy.rpc.netty.demo.encode;

import com.wuqy.rpc.netty.demo.common.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Administrator on 2018/7/6.
 * 发送消息之后，进行encode
 */
public class TimeEncoder2 extends MessageToByteEncoder<UnixTime> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
        out.writeInt((int)msg.value());
    }
}
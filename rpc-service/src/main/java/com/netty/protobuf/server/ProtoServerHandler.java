package com.netty.protobuf.server;

/**
 * Created by Administrator on 2018/7/11.
 */

import com.netty.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoServerHandler extends SimpleChannelInboundHandler<DataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {
        System.out.println(msg.getAddress());
        System.out.println(msg.getAge());
        System.out.println(msg.getName());

        DataInfo.Student bank = DataInfo.Student.newBuilder().
                setName("张三").setAge(20).setAddress("北京").build();

        ctx.channel().writeAndFlush(bank);
    }
}

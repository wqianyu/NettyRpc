package com.netty.protobuf.client;

/**
 * Created by Administrator on 2018/7/11.
 */

import com.netty.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoClientHandler extends SimpleChannelInboundHandler<DataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {
        System.out.println(msg.getName());
        System.out.println(msg.getAddress());
        System.out.println(msg.getAge());
        System.out.println(ctx.channel().remoteAddress());
        System.out.println(ctx.channel().localAddress());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DataInfo.Student user = DataInfo.Student.newBuilder().
                setName("张三2").setAge(22).setAddress("北京2").build();
        ctx.channel().writeAndFlush(user);
    }
}

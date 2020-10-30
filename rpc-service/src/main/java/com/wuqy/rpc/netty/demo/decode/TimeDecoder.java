package com.wuqy.rpc.netty.demo.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 * 数据解包，解决数据传输分片问题
 */
public class TimeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)收到新数据会自动调用该方法
        if (in.readableBytes() < 4) {
            return; // (3)如果数据字节数不足，直接返回，任何事都不做；等待下次数据再过来的时候再一起处理
        }

        out.add(in.readBytes(4)); // (4)out表明decode解码成功，如果不out，会一直调用decode，直到out的内容为空
    }
}

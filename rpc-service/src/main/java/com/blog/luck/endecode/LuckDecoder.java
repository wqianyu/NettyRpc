package com.blog.luck.endecode;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Administrator on 2018/7/9.
 */
public class LuckDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 获取协议的版本
        int version = in.readInt();
        // 获取消息长度
        int contentLength = in.readInt();
        // 获取SessionId
        byte[] sessionByte = new byte[36];
        in.readBytes(sessionByte);
        String sessionId = new String(sessionByte);

        // 组装协议头
        LuckHeader header = new LuckHeader(version, contentLength, sessionId);

        int length = in.readInt();
        // 读取消息内容
        //byte[] content = in.readBytes(in.readableBytes()).array();//数据在buffer中，会报direct buffer错误

        byte[] content = new byte[length];
        //in.writeBytes(content);
       // LuckMessage message = new LuckMessage(header, new String(content));//会报指针越界错误

        String data = new String(ByteBufUtil.getBytes(in.readBytes(length)), Charset.forName("UTF-8"));//正确
        //String data = new String(ByteBufUtil.getBytes(in.readBytes(in.readableBytes())), Charset.forName("UTF-8"));//正确
        LuckMessage message = new LuckMessage(header, data);

        out.add(message);
    }
}

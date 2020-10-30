package com.blog.luck.endecode;

import com.blog.luck.protocol.LuckHeader;
import com.blog.luck.protocol.LuckMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Administrator on 2018/7/9.
 */
@ChannelHandler.Sharable
public class LuckEncoder extends MessageToByteEncoder<LuckMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, LuckMessage message, ByteBuf out) throws Exception {

        // 将Message转换成二进制数据
        LuckHeader header = message.getLuckHeader();

        // 这里写入的顺序就是协议的顺序.

        // 写入Header信息
        out.writeInt(header.getVersion());
        out.writeInt(message.getContent().length());
        ByteBuf sessionIdByte = ctx.alloc().buffer(36).writeBytes(header.getSessionId().getBytes());
        out.writeBytes(sessionIdByte);

        //out.writeBytes(header.getSessionId().getBytes());
        // 写入消息主体信息
        out.writeInt(message.getContent().getBytes().length);
        out.writeBytes(message.getContent().getBytes());
    }
}

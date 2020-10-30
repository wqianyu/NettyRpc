package com.netty.pool7.trigger;

import com.netty.pool7.vo.HeatBeatMsg;
import com.netty.pool7.vo.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * 当客户端idle时间内没有向server发送请求时，立即向服务器端发送心跳
 * Created by wuqy on 2018/8/10.
 */
@ChannelHandler.Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
            CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                //ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());//不会进行编解码(addLast顺序有关)
                RpcRequest req = new HeatBeatMsg().getHeartBeatReq();
                System.out.println("client userEventTriggered heartbeat:"+req.toString());
                ctx.writeAndFlush(req);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

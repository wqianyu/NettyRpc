package com.netty.pool4.client;

import com.netty.pool4.SpringUtil;
import com.netty.pool4.halb.HeartBeatService;
import com.netty.pool4.halb.ServerInfo;
import com.netty.pool4.halb.ServiceAddress;
import com.netty.pool4.vo.HeatBeatMsg;
import com.netty.pool4.vo.MsgType;
import com.netty.pool4.vo.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/7/17.
 */
//@Component
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

    static AtomicInteger count = new AtomicInteger(1);

    //@Autowired
    private ServiceAddress serviceAddress;

    private HeartBeatService heartBeatService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse response = (RpcResponse) msg;
        System.out.println(count.getAndIncrement() + ":" + response);
        if(response.getMsgType() == MsgType.MSG) {
            if (NettyClient.islock(response.getMessageId())) {//判断对象锁是否还存在（超时是会被移除）丢弃结果
                NettyClient.addResponse(response);//未超时，添加到消息队列，唤醒阻塞线程处理
                NettyClient.callNotify(response.getMessageId());
            }
        } else {
            if (null == serviceAddress) {
                serviceAddress = SpringUtil.getBean(ServiceAddress.class);
            }
            serviceAddress.receive((HeatBeatMsg)response.getResponse());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("=============channelInactive===============");
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        String remoteAddressStr = remoteAddress.toString();
        System.out.println(remoteAddressStr);
        if (null == serviceAddress) {
            serviceAddress = SpringUtil.getBean(ServiceAddress.class);
        }
        String host[] = remoteAddressStr.split(":");
        String ip = "";
        String port = "";
        if(host.length == 2) {
            ip = host[0].substring(1);
            port = host[1];
        }
        serviceAddress.inactive(new ServerInfo(ip, Integer.parseInt(port)));
        LOGGER.info("可用连接："+serviceAddress.getActiveAddress()+",不可用连接："+serviceAddress.getInactiveAddress());
        System.out.println("=============channelInactive===============");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*System.out.println("=======exceptionCaught====== heartBeat");
        if (null == heartBeatService) {
            heartBeatService = SpringUtil.getBean(HeartBeatService.class);
        }
        heartBeatService.heartbeat();*/
    }
}
package com.netty.pool5.client;

import com.netty.pool5.ConnectionWatchdog;
import com.netty.pool5.encode.RequestEncoder;
import com.netty.pool5.encode.ResponseDecoder;
import com.netty.pool5.trigger.ConnectorIdleStateTrigger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/17.
 */
public class NettyChannelPoolHandler implements ChannelPoolHandler {

    protected final Timer timer;

    private Bootstrap boot;

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    private String host;

    private int port;

    private boolean reconnect;


    ConnectionWatchdog watchdog;

    public NettyChannelPoolHandler(Bootstrap boot, Timer timer, int port, String host, boolean reconnect) {
        this.boot = boot;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
        watchdog = new ConnectionWatchdog(boot, timer, port, host, true) {
            public ChannelHandler[] handlers() {
                return new ChannelHandler[] {
                        this,
                        new RequestEncoder(),
                        new ResponseDecoder(),
                        new NettyClientHandler(),
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        idleStateTrigger
                };
            }
        };
    }

    @Override
    public void channelReleased(Channel ch) throws Exception {
        System.out.println("channelReleased. Channel ID: " + ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        System.out.println("channelAcquired. Channel ID: " + ch.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        System.out.println("channelCreated. Channel ID: " + ch.id());
        SocketChannel channel = (SocketChannel) ch;
        channel.pipeline().addLast(watchdog.handlers());
    }
}
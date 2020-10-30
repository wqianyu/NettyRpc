package com.netty.pool4.vo;

import com.netty.pool4.halb.ServerInfo;

/**
 * Created by Administrator on 2018/8/1.
 */
public class HeatBeatMsg {
    private MsgType msgType = MsgType.HEART_BEAT;

    private Long millis = System.currentTimeMillis();

    private String serviceName;

    private ServerInfo serverInfo;

    public HeatBeatMsg() {

    }

    public HeatBeatMsg(String serviceName) {
        this.serviceName = serviceName;
    }

    public static RpcRequest getHeartBeatReq(ServerInfo serverInfo, String serviceName) {
        RpcRequest request = new RpcRequest();
        request.setHeartBeat();
        HeatBeatMsg msg = new HeatBeatMsg(serviceName);
        msg.setServerInfo(serverInfo);
        request.setObject(msg);
        return request;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Long getMillis() {
        return millis;
    }

    public void setMillis(Long millis) {
        this.millis = millis;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "HeatBeatMsg{" +
                "msgType=" + msgType +
                ", millis=" + millis +
                ", serviceName='" + serviceName + '\'' +
                ", serverInfo=" + serverInfo +
                '}';
    }
}

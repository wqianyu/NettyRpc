package com.netty.pool6.vo;

/**
 * Created by Administrator on 2018/8/1.
 */
public class HeatBeatMsg {
    private MsgType msgType = MsgType.HEART_BEAT;

    public HeatBeatMsg() {

    }

    public static RpcRequest getHeartBeatReq() {
        RpcRequest request = new RpcRequest();
        request.setHeartBeat();
        HeatBeatMsg msg = new HeatBeatMsg();
        request.setObject(msg);
        return request;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "HeatBeatMsg{" +
                "msgType=" + msgType +
                '}';
    }
}

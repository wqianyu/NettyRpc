package com.netty.pool4.vo;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/13.
 */
public class RpcRequest<T> {
    private int intefId;

    private int sid;

    private MsgType msgType = MsgType.MSG;

    private T object;

    private String messageId;

    public RpcRequest() {
        messageId = UUID.randomUUID()+"";
    }

    public RpcRequest(int intefId, int sid, T object) {
        this.intefId = intefId;
        this.sid = sid;
        this.object = object;
    }

    public String getMessageId() {
        return messageId;
    }

    public int getIntefId() {
        return intefId;
    }

    public void setIntefId(int intefId) {
        this.intefId = intefId;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public void setHeartBeat() {
        this.msgType = MsgType.HEART_BEAT;
    }

    public void setMsg() {
        this.msgType = MsgType.MSG;
    }

    public MsgType getMsgType() {
        return this.msgType;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "intefId=" + intefId +
                ", sid=" + sid +
                ", object=" + object +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}

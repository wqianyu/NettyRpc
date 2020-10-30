package com.netty.pool7.vo;

/**
 * Created by Administrator on 2018/7/13.
 */
public class RpcResponse<T> {
    private int statusCode = 200;

    private T response;

    private String messageId;

    private MsgType msgType;

    public RpcResponse(){

    }

    public RpcResponse(int statusCode, T response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "statusCode=" + statusCode +
                ", response=" + response +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}

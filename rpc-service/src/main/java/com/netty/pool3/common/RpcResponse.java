package com.netty.pool3.common;

/**
 * Created by Administrator on 2018/7/13.
 */
public class RpcResponse<T> {
    private int statusCode = 200;

    private T response;

    private String messageId;

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

    @Override
    public String toString() {
        return "RpcResponse{" +
                "statusCode=" + statusCode +
                ", response=" + response +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}

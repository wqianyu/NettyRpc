package com.blog.luck.common;

/**
 * Created by Administrator on 2018/7/13.
 */
public class RpcResponse<T> {
    private int statusCode = 200;

    private T response;

    public RpcResponse(){

    }

    public RpcResponse(int statusCode, T response) {
        this.statusCode = statusCode;
        this.response = response;
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
                '}';
    }
}

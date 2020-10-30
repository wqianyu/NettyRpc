package com.blog.luck.common;

/**
 * Created by Administrator on 2018/7/13.
 */
public class RpcRequest<T> {
    private int intefId;

    private int sid;

    private T object;

    public RpcRequest() {
    }

    public RpcRequest(int intefId, int sid, T object) {
        this.intefId = intefId;
        this.sid = sid;
        this.object = object;
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

    @Override
    public String toString() {
        return "RpcRequest{" +
                "intefId=" + intefId +
                ", sid=" + sid +
                ", object=" + object +
                '}';
    }
}

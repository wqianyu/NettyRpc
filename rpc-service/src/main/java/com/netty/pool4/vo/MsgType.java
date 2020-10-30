package com.netty.pool4.vo;

/**
 * Created by Administrator on 2018/8/2.
 */
public enum MsgType {
    HEART_BEAT(0,"heartBeat"),
    MSG(1,"msg");

    private int value;
    private String name;

    MsgType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.netty.pool5.halb;

/**
 * Created by Administrator on 2018/8/2.
 */
public class ServerInfo {
    private String ip;

    private int port;

    private int weight;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public ServerInfo(){

    }

    public ServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int hashCode() {
        if (this instanceof ServerInfo) {
            ServerInfo serverInfo = (ServerInfo) this;
            return (serverInfo.ip + serverInfo.port).hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerInfo) {
            ServerInfo info = (ServerInfo) obj;
            return ip.equals(info.getIp())&&port==info.getPort();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}

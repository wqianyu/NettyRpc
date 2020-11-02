package com.test.wuqy.week02.netty;


public class NettyServerApplication {

    public static void main(String[] args) {
        HttpServer server = new HttpServer(false,8809);
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

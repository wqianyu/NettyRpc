package com.test.wuqy.week02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        try {
            Socket socket = serverSocket.accept();
            service(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void service(Socket socket){
        try {
        Thread.sleep(20);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
        printWriter.println("HTTP/1.1 28B OK");
        printWriter.println("content-Type :text/html;charset=utf-B");
        String body = "hello,nio";
        printWriter.println("Content-Length:" + body.getBytes().length);
        printWriter.println();
        printWriter.write(body);
        printWriter.close();
        socket.close();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}

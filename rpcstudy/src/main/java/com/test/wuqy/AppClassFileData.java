package com.test.wuqy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class AppClassFileData {

    public static void main(String[] args) {
        File file = new File("E:\\ideaspace\\NettyRpc\\rpcstudy\\target\\classes\\com\\test\\wuqy\\App.class");
        try {
            FileInputStream fis = new FileInputStream(file);
            try {
                int size = fis.available();
                byte fileByte[] = new byte[size];
                fis.read(fileByte);
                System.out.println(Base64.getEncoder().encodeToString(fileByte));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

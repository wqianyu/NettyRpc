package com.test.wuqy.week01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，
 * 执行 hello 方法，此文件内容是一个 Hello.class 文件
 * 所有字节（x=255-x）处理后的文件
 */
public class HelloClassLoader extends ClassLoader{

    public static void main(String[] args) {
        try {
            Class<?> clazz = new HelloClassLoader().findClass("Hello");
            Method method = clazz.getMethod("hello");
            method.invoke(clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String helloFilePath = "E:\\ideaspace\\NettyRpc\\rpcstudy\\src\\main\\java\\com\\test\\wuqy\\week01\\Hello.xlass";
        File helloFile = new File(helloFilePath);
        try {
            FileInputStream fis = new FileInputStream(helloFile);
            int size = fis.available();
            byte[] fisByte = new byte[size];
            fis.read(fisByte);
            for (int index = 0; index < fisByte.length; index++) {
                fisByte[index] = (byte)(255 - (int)fisByte[index]);
            }
            return defineClass(name, fisByte, 0, fisByte.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

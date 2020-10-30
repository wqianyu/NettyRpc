package com.test.wuqy;

import java.util.Base64;

public class AppClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            //不需要class文件就可以直接加载类
            new AppClassLoader().findClass("com.test.wuqy.App").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String appBase64 = "yv66vgAAADQAIwoABgAVCQAWABcIABgKABkAGgcAGwcAHAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQATTGNvbS90ZXN0L3d1cXkvQXBwOwEABG1haW4BABYoW0xqYXZhL2xhbmcvU3RyaW5nOylWAQAEYXJncwEAE1tMamF2YS9sYW5nL1N0cmluZzsBAAg8Y2xpbml0PgEAClNvdXJjZUZpbGUBAAhBcHAuamF2YQwABwAIBwAdDAAeAB8BAAxIZWxsbyBXb3JsZCEHACAMACEAIgEAEWNvbS90ZXN0L3d1cXkvQXBwAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TeXN0ZW0BAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsBABNqYXZhL2lvL1ByaW50U3RyZWFtAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgAhAAUABgAAAAAAAwABAAcACAABAAkAAAAvAAEAAQAAAAUqtwABsQAAAAIACgAAAAYAAQAAAAcACwAAAAwAAQAAAAUADAANAAAACQAOAA8AAQAJAAAANwACAAEAAAAJsgACEgO2AASxAAAAAgAKAAAACgACAAAADwAIABAACwAAAAwAAQAAAAkAEAARAAAACAASAAgAAQAJAAAAJQACAAAAAAAJsgACEgO2AASxAAAAAQAKAAAACgACAAAACgAIAAsAAQATAAAAAgAU";
        byte[] bytes = decode(appBase64);
        //return super.findClass(name);
        return defineClass(name, bytes, 0, bytes.length);
    }

    public byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}

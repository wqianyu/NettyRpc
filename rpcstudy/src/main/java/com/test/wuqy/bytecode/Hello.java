package com.test.wuqy.bytecode;

public class Hello {
    public static void main(String[] args) {
        int a = 10, b =3;
        int c = (a+b)*(a-3);
        for(int i = b; b < 10; b++) {
            if(b % 8 == 0) {
                break;
            }
        }
    }
}

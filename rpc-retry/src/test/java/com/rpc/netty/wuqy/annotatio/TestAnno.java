package com.rpc.netty.wuqy.annotatio;

import com.rpc.netty.wuqy.annotation.AnnoTationTest;
import org.junit.Test;

public class TestAnno {

    @Test
    public void runTest() {
        try {
            new AnnoTationTest().fiveTimes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

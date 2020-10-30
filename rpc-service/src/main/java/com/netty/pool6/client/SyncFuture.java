package com.netty.pool6.client;


import java.util.concurrent.*;

/**
 * netty异步返回转化为同步返回：messageId
 * Created by wuqy on 2018/8/27.
 */
public class SyncFuture<T> implements Future<T> {

    // 因为请求和响应是一一对应的，因此初始化CountDownLatch值为1。
    private CountDownLatch latch = new CountDownLatch(1);

    // 需要响应线程设置的响应结果
    private T response;

    // Futrue的请求时间，用于计算Future是否超时
    private long beginTime = System.currentTimeMillis();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if(null == response)
            return false;
        return true;
    }

    // 获取响应结果，直到有结果才返回。
    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    // 获取响应结果，直到有结果或者超过指定时间就返回。
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout,unit);
        return response;
    }

    // 用于设置响应结果，并且做countDown操作，通知请求线程
    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }

    public long getBeginTime() {
        return beginTime;
    }
}

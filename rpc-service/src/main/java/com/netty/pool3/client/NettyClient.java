package com.netty.pool3.client;

import com.netty.pool3.common.RpcRequest;
import com.netty.pool3.common.RpcResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/7/20.
 */
public class NettyClient {

    private final Integer maxTimeout = 10;

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private static ConcurrentHashMap<String, RpcResponse> receiveList = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Object> lock = new ConcurrentHashMap<>();

    //private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    public static RpcResponse getResponseData(RpcRequest request) {
        if (receiveList.containsKey(request.getMessageId())) {
            return receiveList.get(request.getMessageId());
        }
        return null;
    }

    public static void addResponse(RpcResponse response) {
        receiveList.put(response.getMessageId(), response);
    }

    public static void removeRsp(RpcResponse response) {
        receiveList.remove(response.getMessageId());
    }

    public static boolean isContains(RpcResponse response) {
        return receiveList.containsKey(response.getMessageId());
    }

    public RpcResponse send(Channel channel, RpcRequest request) {
        RpcResponse response = null;
        ScheduledFuture scheduledFuture = null;
        ScheduledExecutorService scheduledExecutorService = null;
        try {
            lock.put(request.getMessageId(), new Object());
            channel.writeAndFlush(request);
            response = getResponseData(request);//获取返回结果
            long begin = System.currentTimeMillis();
            if (null == response) {//返回结果还未返回
                scheduledExecutorService = new ScheduledThreadPoolExecutor(5);
                scheduledFuture = scheduledExecutorService.schedule(//设定超时唤醒阻塞线程任务，防止server一直不返回，阻塞客户端
                        new Callable() {
                            public Object call() throws Exception {
                                System.out.println("---------------------------------- call "+lockSize()+ " "+size()+"---------------------------------------");
                                Object object = lock.get(request.getMessageId());
                                if (null != object) {//如果客户端还阻塞，唤醒客户端，否则直接返回
                                    LOGGER.info("_________call notify_______");
                                    synchronized (lock.get(request.getMessageId())) {
                                        lock.get(request.getMessageId()).notify();
                                    }
                                }
                                RpcResponse ret = getResponseData(request);
                                return ret;
                            }
                        },
                        maxTimeout,
                        TimeUnit.SECONDS);//延迟maxTimeout秒执行,异步的
                LOGGER.info("_________waiting_______");
                begin = System.currentTimeMillis();
                synchronized (lock.get(request.getMessageId())) {
                    lock.get(request.getMessageId()).wait();//阻塞，等待接收时候唤醒或者超时唤醒
                }
            }
            LOGGER.info("_________active_______");
            boolean state = false;
            //if (null == response) {
                while(!state && (maxTimeout*1000 > System.currentTimeMillis()-begin)) {//超时时间内监听返回结果（只执行一次，已经有返回结果或者已超时）
                    response = getResponseData(request);
                    if (null == response) {
                        Thread.sleep(10);//为空延迟10ms再获取
                        continue;
                    } else {
                        state = true;
                        removeRsp(response);
                    }
                }
                if (null == response) {
                    //response = (RpcResponse)scheduledFuture.get();
                }
                //response = (RpcResponse)scheduledFuture.get();
                //scheduledExecutorService.shutdown();
            //}
            if (!state) {//超时抛出异常
                response = (RpcResponse)scheduledFuture.get();
                if (null == response) {
                    throw new Exception("time out");
                }
            } else {
                if (null != scheduledFuture) {
                    scheduledFuture.cancel(true);
                }
            }
            if (isContains(response)) {//清理过期的数据
                removeRsp(response);
            }
        } catch (InterruptedException e) {
            LOGGER.warn("send error InterruptedException", e);
        } catch (Exception e) {
            LOGGER.warn("send error ExecutionException", e);
        } finally {
            LOGGER.info(" ====== finally ===== "+lockSize()+ " "+size());
            lock.remove(request.getMessageId());
            LOGGER.info(" ====== finally ===== "+lockSize()+ " "+size());
            if (null != scheduledExecutorService) {
                scheduledExecutorService.shutdown();
            }
        }
        return response;
    }

    public static void callNotify(String messageId) {//接收消息线程唤醒处理线程
        Object object = lock.get(messageId);
        if (null != object) {
            LOGGER.info("_________notify_______");
            synchronized (object) {
                object.notify();
            }
        } else {
            LOGGER.info("_________no found_______");
        }
    }

    public static int size() {
        return receiveList.size();
    }

    public static int lockSize() {
        return lock.size();
    }

    public static boolean islock(String messageId) {
        return lock.containsKey(messageId);
    }
}

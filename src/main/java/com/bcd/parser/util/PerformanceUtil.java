package com.bcd.parser.util;

import com.bcd.parser.Parser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceUtil {

    static Logger logger= LoggerFactory.getLogger(PerformanceUtil.class);

    /**
     * 测试单个线程
     * @param data
     * @param parser
     * @param clazz
     * @param num
     * @param <T>
     */
    public static <T>void testSingleThreadPerformance(String data,Parser parser,Class<T> clazz,int num){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        long t1=System.currentTimeMillis();
        for(int i=1;i<=num;i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            parser.parse(clazz,byteBuf);
        }
        long t2=System.currentTimeMillis();
        long diff=t2-t1;
        logger.info("num:{} , cost time:{}ms , speed:{}/s",num,diff,(int)(num/(diff/1000d)));
    }


    /**
     * 测试多线程
     * @param data
     * @param parser
     * @param clazz
     * @param poolSize
     * @param num
     * @param <T>
     */
    public static <T>void testPerformance(String data, Parser parser,Class<T> clazz,int poolSize,int num){
        logger.info("poolSize:{}",poolSize);
        AtomicInteger count=new AtomicInteger(0);

        ExecutorService[]pools=new ExecutorService[poolSize];
        for(int i=0;i<pools.length;i++){
            pools[i] = Executors.newSingleThreadExecutor();
        }
        for (ExecutorService pool : pools) {
            pool.execute(() -> {
                testParse(data, parser,clazz,num, count);
            });
        }

        ScheduledExecutorService monitor=Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(()->{
            int cur=count.getAndSet(0)/3;
            logger.info("core:{} , speed/s:{} , avgSpeed/s:{}",poolSize,cur,cur/poolSize);
        },3,3,TimeUnit.SECONDS);

        try {
            for (ExecutorService pool : pools) {
                pool.shutdown();
            }
            for (ExecutorService pool : pools) {
                pool.awaitTermination(1,TimeUnit.HOURS);
            }
            monitor.shutdown();
            monitor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("interrupted",e);
        }
    }

    private static <T>void testParse(String data, Parser parser,Class<T> clazz, int num, AtomicInteger count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for(int i=1;i<=num;i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            parser.parse(clazz,byteBuf);
            count.incrementAndGet();
        }
    }

    private static <T>void testDeParse(String data, Parser parser,Class<T> clazz, int num, AtomicInteger count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        T packet= parser.parse(clazz, byteBuf);
        ByteBuf res= Unpooled.buffer(bytes.length);
        res.markReaderIndex();
        res.markWriterIndex();
        for(int i=1;i<=num;i++) {
            res.resetReaderIndex();
            res.resetWriterIndex();
            parser.deParse(packet,res);
            count.incrementAndGet();
        }
    }
}

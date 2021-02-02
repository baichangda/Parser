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
import java.util.concurrent.atomic.LongAdder;

public class PerformanceUtil {

    static Logger logger= LoggerFactory.getLogger(PerformanceUtil.class);

    /**
     * 测试多线程
     * @param data
     * @param parser
     * @param clazz
     * @param threadNum
     * @param num
     * @param <T>
     */
    public static <T>void testMultiThreadPerformance(String data, Parser parser, Class<T> clazz, int threadNum, int num,boolean parse){
        logger.info("threadNum:{}",threadNum);
        long [] count=new long[threadNum];
        ExecutorService[]pools=new ExecutorService[threadNum];
        for(int i=0;i<pools.length;i++){
            pools[i] = Executors.newSingleThreadExecutor();
        }
        for (int i = 0; i < pools.length; i++) {
            final int index=i;
            pools[index].execute(() -> {
                if(parse){
                    testParse(data, parser, clazz, num, count,index);
                }else {
                    testDeParse(data, parser,clazz,num, count,index);
                }
            });
        }

        ScheduledExecutorService monitor=Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(()->{
            long sum=0;
            for (int i = 0; i < count.length; i++) {
                sum+=count[i];
                count[i]=0L;
            }
            logger.info("{} , threadNum:{} , totalSpeed/s:{} , perThreadSpeed/s:{}",parse?"parse":"deParse",threadNum,sum,sum/threadNum);
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

    public static <T>void testParse(String data, Parser parser,Class<T> clazz, int num, long [] count,int index){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for(int i=1;i<=num;i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            T t= parser.parse(clazz,byteBuf);
            count[index]++;
        }
    }

    public static <T>void testDeParse(String data, Parser parser,Class<T> clazz, int num, long[] count,int index){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        T packet= parser.parse(clazz, byteBuf);
        ByteBuf res= Unpooled.buffer(bytes.length);
        for(int i=1;i<=num;i++) {
            res.clear();
            parser.deParse(packet,res);
//            System.out.println(data.toLowerCase());
//            System.out.println(ByteBufUtil.hexDump(res));
            count[index]++;
        }
    }
}

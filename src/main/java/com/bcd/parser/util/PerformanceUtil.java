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
        LongAdder count=new LongAdder();
        ExecutorService[]pools=new ExecutorService[threadNum];
        for(int i=0;i<pools.length;i++){
            pools[i] = Executors.newSingleThreadExecutor();
        }
        for (int i = 0; i < pools.length; i++) {
            pools[i].execute(() -> {
                if(parse){
                    testParse(data, parser, clazz, num, count);
                }else {
                    testDeParse(data, parser,clazz,num, count);
                }
            });
        }

        ScheduledExecutorService monitor=Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(()->{
            long sum=count.sumThenReset()/3;
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

    public static <T>void testParse(String data, Parser parser,Class<T> clazz, int num, LongAdder count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for(int i=1;i<=num;i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            T t= parser.parse(clazz,byteBuf);
            count.increment();
        }
    }

    public static <T>void testDeParse(String data, Parser parser,Class<T> clazz, int num, LongAdder count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        T packet= parser.parse(clazz, byteBuf);
        ByteBuf res= Unpooled.buffer(bytes.length);
        for(int i=1;i<=num;i++) {
            res.clear();
            parser.deParse(packet,res);
//            System.out.println(data.toLowerCase());
//            System.out.println(ByteBufUtil.hexDump(res));
            count.increment();
        }
    }
}

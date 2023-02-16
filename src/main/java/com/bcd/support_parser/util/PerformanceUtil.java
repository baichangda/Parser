package com.bcd.support_parser.util;

import com.bcd.support_parser.Parser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class PerformanceUtil {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceUtil.class);

    /**
     * 测试多线程
     *
     * @param data
     * @param parser
     * @param clazz
     * @param threadNum
     * @param num
     * @param <T>
     */
    public static <T> void testMultiThreadPerformance(String data, Parser parser, Class<T> clazz, int threadNum, int num, boolean parse) {
        logger.info("threadNum:{}", threadNum);
        final LongAdder count = new LongAdder();
        final ExecutorService[] pools = new ExecutorService[threadNum];
        for (int i = 0; i < pools.length; i++) {
            pools[i] = Executors.newSingleThreadExecutor();
        }
        if (parse) {
            for (int i = 0; i < pools.length; i++) {
                pools[i].execute(() -> {
                    testParse(data, parser, clazz, num, count);
                });
            }
        } else {
            final ByteBuf buf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(data));
            final T t = parser.parse(clazz, buf, null);
            for (int i = 0; i < pools.length; i++) {
                pools[i].execute(() -> {
                    testDeParse(t, parser, num, count);
                });
            }
        }
        final ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(() -> {
            long sum = count.sumThenReset() / 3;
            logger.info("{} , threadNum:{} , totalSpeed/s:{} , perThreadSpeed/s:{}", "parse", threadNum, sum, sum / threadNum);
        }, 3, 3, TimeUnit.SECONDS);

        try {
            for (ExecutorService pool : pools) {
                pool.shutdown();
            }
            for (ExecutorService pool : pools) {
                pool.awaitTermination(1, TimeUnit.HOURS);
            }
            monitor.shutdown();
            monitor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("interrupted", e);
        }
    }

    public static <T> void testParse(String data, Parser parser, Class<T> clazz, int num, LongAdder count) {
        final byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for (int i = 1; i <= num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            final T t = parser.parse(clazz, byteBuf, null);
            count.increment();
        }
    }

    public static <T> void testDeParse(Object obj, Parser parser, int num, LongAdder count) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for (int i = 1; i <= num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            parser.deParse(obj, byteBuf, null);
            count.increment();
        }
    }

}

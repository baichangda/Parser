package com.bcd.support_parser.impl.gb32960;

import com.bcd.support_parser.Parser;
import com.bcd.support_parser.impl.gb32960.data.Packet;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ParserUtil;
import com.bcd.support_parser.util.PerformanceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class Parser_gb32960 extends Parser {

    static Logger logger = LoggerFactory.getLogger(Parser_gb32960.class);

    public Parser_gb32960(boolean printStack) {
        super(printStack);
    }

    @Override
    protected List<Class> getParsableClass() {
        return ParserUtil.getParsableClassByScanPackage("com.bcd.support_parser.impl.gb32960");
    }

    @Override
    protected List<FieldProcessor> initExtProcessor() {
        return super.initProcessorByScanPackage("com.bcd.support_parser.impl.gb32960");
    }

    public static void main(String[] args) {
        Parser_gb32960 parser_gb32960 = new Parser_gb32960(false);
        parser_gb32960.init();
        parser_gb32960.performanceTest("232302fe4c534a4532343036364a4732323935383901011f14090812110c01020101000000031fec0e8026bb45021013dd0000050007381f0701d8cc8c06013e0f20010c0f1701054e01074c070000000000000000000801010e8126bb00600001600f1b0f1b0f190f1a0f1a0f1a0f1a0f180f1a0f1a0f1b0f170f1b0f1b0f1b0f1c0f190f1a0f1a0f1b0f1a0f1a0f1a0f1a0f1a0f1a0f1a0f190f1a0f190f1b0f1a0f1b0f1a0f190f1a0f1b0f1b0f1a0f1b0f1a0f1d0f1a0f1a0f1b0f1c0f1d0f1d0f1c0f1b0f1b0f1c0f1a0f1d0f1d0f1c0f1d0f1d0f1b0f1b0f1a0f200f1d0f1a0f1a0f1a0f1a0f1b0f1b0f1b0f1a0f1c0f1b0f1a0f1a0f1a0f1b0f1e0f1d0f1b0f1c0f1d0f1d0f1d0f1d0f1c0f1b0f1b0f1d0f1c0f1c0f1c0f1c0f1d0f1b0f1d09010100104d4d4d4d4e4d4c4c4d4c4d4d4d4d4d4c2a",
                1,
                true);
    }

    public void performanceTest(String data, int threadNum, boolean parse) {
        logger.info("param threadNum[{}]", threadNum);
        PerformanceUtil.testMultiThreadPerformance(data, this, Packet.class, threadNum, Integer.MAX_VALUE, parse);
    }

    private static void parseToPacket(String data, int num, AtomicInteger count) {
        byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for (int i = 0; i < num; i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            Packet packet = new Packet();

            byte[] header = new byte[2];
            byteBuf.readBytes(header);
            packet.header = header;

            packet.flag = byteBuf.readUnsignedByte();

            packet.replyFlag = byteBuf.readUnsignedByte();

            int discardLen = 0;
            byte[] vinBytes = new byte[17];
            byteBuf.readBytes(vinBytes);
            for (int j = vinBytes.length - 1; j >= 0; j--) {
                if (vinBytes[j] == 0) {
                    discardLen++;
                } else {
                    break;
                }
            }
            String vin = new String(vinBytes, 0, vinBytes.length - discardLen);
            packet.vin = vin;

            packet.encodeWay = byteBuf.readUnsignedByte();

            packet.contentLength = byteBuf.readUnsignedShort();

            byte[] dataContent = new byte[packet.contentLength];
            byteBuf.readBytes(dataContent);
            packet.dataContent = dataContent;

            packet.code = byteBuf.readByte();

            count.incrementAndGet();
        }
    }

    /**
     * 手动解析 测试多线程
     *
     * @param data
     * @param threadNum
     * @param num
     */
    private static void testMultiThreadPerformance(String data, int threadNum, int num) {
        logger.info("threadNum:{}", threadNum);
        AtomicInteger count = new AtomicInteger(0);
        ExecutorService[] pools = new ExecutorService[threadNum];
        for (int i = 0; i < pools.length; i++) {
            pools[i] = Executors.newSingleThreadExecutor();
        }
        for (ExecutorService pool : pools) {
            pool.execute(() -> {
                parseToPacket(data, num, count);
            });
        }

        ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(() -> {
            int cur = count.getAndSet(0) / 3;
            logger.info("threadNum:{} , totalSpeed/s:{} , perThreadSpeed/s:{}", threadNum, cur, cur / threadNum);
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


}

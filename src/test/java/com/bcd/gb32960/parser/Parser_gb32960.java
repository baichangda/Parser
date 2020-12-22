package com.bcd.gb32960.parser;

import com.bcd.gb32960.data.Packet;
import com.bcd.parser.Parser;
import com.bcd.parser.process.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
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

public class Parser_gb32960 extends Parser{

    static Logger logger= LoggerFactory.getLogger(Parser_gb32960.class);

    public Parser_gb32960() {
    }

    @Override
    protected List<Class> getParsableClass() {
        return ParserUtil.getParsableClassByScanPackage("com.bcd");
    }

    @Override
    protected List<FieldProcessor> initExtProcessor() {
        return super.initProcessorByScanClass("com.bcd");
    }

    public static void main(String[] args) throws Exception{
        Parser parser= new Parser_gb32960();
//        parser.setEnableOffsetField(true);
        parser.init();
        String data="232303FE4C534A4132343033304853313932393639010135" +
                "1403190F0507010203010000000469B00EE5271055020F1FFF000002010103424E1E4E2045FFFF2710050006BE437001CF306A060160FFFF0101FFFF0118FF01010E070000000000000000000801010EE527100060000160FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF09010100180EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFED";
        testSingleThreadPerformance(data,parser);
//        testPerformance(data,parser);

    }

    private static void testSingleThreadPerformance(String data,Parser parser){
        int num=3000000;
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        long t1=System.currentTimeMillis();
        for(int i=1;i<=num;i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            parser.parse(Packet.class,byteBuf);
        }
        long t2=System.currentTimeMillis();
        long diff=t2-t1;
        logger.info("num:{} , cost time:{}ms , speed:{}/s",num,diff,(int)(num/(diff/1000d)));
    }

    private static void testPerformance(String data, Parser parser){
//        int poolSize=Runtime.getRuntime().availableProcessors()+1;
        int poolSize=1;
        int num=10000000;
        logger.info("poolSize:{}",poolSize);
        AtomicInteger count=new AtomicInteger(0);

        ExecutorService []pools=new ExecutorService[poolSize];
        for(int i=0;i<pools.length;i++){
            pools[i] = Executors.newSingleThreadExecutor();
        }
        for (ExecutorService pool : pools) {
            pool.execute(() -> {
                testParse(data, parser,num, count);
            });
        }

        ScheduledExecutorService monitor=Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(()->{
            logger.info("speed/s:{}",count.getAndSet(0)/3);
        },3,3,TimeUnit.SECONDS);

        try {
            for (ExecutorService pool : pools) {
                pool.shutdown();
            }
            for (ExecutorService pool : pools) {
                pool.awaitTermination(1,TimeUnit.HOURS);
            }
            monitor.shutdown();
            monitor.awaitTermination(1,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("interrupted",e);
        }
    }

    private static void testParse(String data, Parser parser, int num, AtomicInteger count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for(int i=1;i<=num;i++) {
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            parser.parse(Packet.class,byteBuf);
            count.incrementAndGet();
        }
    }

    private static void testDeParse(String data, Parser parser, int num, AtomicInteger count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        Packet packet= parser.parse(Packet.class, byteBuf);
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

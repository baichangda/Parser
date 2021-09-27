package com.bcd.parser.impl.gb32960;

import com.bcd.parser.Parser;
import com.bcd.parser.impl.gb32960.data.Packet;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.PerformanceUtil;
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

    public Parser_gb32960(boolean printStack) {
        super(printStack);
    }

    @Override
    protected List<Class> getParsableClass() {
        return ParserUtil.getParsableClassByScanPackage("com.bcd.parser.impl.gb32960");
    }

    @Override
    protected List<FieldProcessor> initExtProcessor() {
        return super.initProcessorByScanPackage("com.bcd.parser.impl.gb32960");
    }

    public static void main(String[] args){
        Parser parser= new Parser_gb32960(false);
        parser.init();

        String data="232303FE4C534A4132343033304853313932393639010135" +
                "1403190F0507010203010000000469B00EE5271055020F1FFF000002010103424E1E4E2045FFFF2710050006BE437001CF306A060160FFFF0101FFFF0118FF01010E070000000000000000000801010EE527100060000160FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF09010100180EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFED";
        int threadNum=1;
        if(args.length>=1){
            threadNum=Integer.parseInt(args[0]);
        }
        logger.info("param threadNum[{}]",threadNum);
        int num=100000000;
        PerformanceUtil.testMultiThreadPerformance(data,parser,Packet.class,threadNum,num,true);
//        testMultiThreadPerformance(data,threadNum,num);
//        PerformanceUtil.testDeParse(data,parser,Packet.class,1,new LongAdder());
//        PerformanceUtil.testParse(data,parser,Packet.class,1,new LongAdder());
    }

    private static void parseToPacket(String data,int num,AtomicInteger count){
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();
        for(int i=0;i<num;i++){
            byteBuf.resetReaderIndex();
            byteBuf.resetWriterIndex();
            Packet packet=new Packet();

            byte[] header=new byte[2];
            byteBuf.readBytes(header);
            packet.setHeader(header);

            packet.setFlag(byteBuf.readUnsignedByte());

            packet.setReplyFlag(byteBuf.readUnsignedByte());

            int discardLen=0;
            byte[] vinBytes=new byte[17];
            byteBuf.readBytes(vinBytes);
            for(int j=vinBytes.length-1;j>=0;j--){
                if(vinBytes[j]==0){
                    discardLen++;
                }else{
                    break;
                }
            }
            String vin=new String(vinBytes,0,vinBytes.length-discardLen);
            packet.setVin(vin);

            packet.setEncodeWay(byteBuf.readUnsignedByte());

            packet.setContentLength(byteBuf.readUnsignedShort());

            byte[] dataContent=new byte[packet.getContentLength()];
            byteBuf.readBytes(dataContent);
            packet.setDataContent(dataContent);

            packet.setCode(byteBuf.readByte());

            count.incrementAndGet();
        }
    }

    /**
     * 手动解析 测试多线程
     * @param data
     * @param threadNum
     * @param num
     */
    private static void testMultiThreadPerformance(String data,int threadNum,int num){
        logger.info("threadNum:{}",threadNum);
        AtomicInteger count=new AtomicInteger(0);
        ExecutorService[]pools=new ExecutorService[threadNum];
        for(int i=0;i<pools.length;i++){
            pools[i] = Executors.newSingleThreadExecutor();
        }
        for (ExecutorService pool : pools) {
            pool.execute(() -> {
                parseToPacket(data,num,count);
            });
        }

        ScheduledExecutorService monitor=Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(()->{
            int cur=count.getAndSet(0)/3;
            logger.info("threadNum:{} , totalSpeed/s:{} , perThreadSpeed/s:{}",threadNum,cur,cur/threadNum);
        },3,3, TimeUnit.SECONDS);

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





}

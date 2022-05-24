package com.bcd.support_parser.impl.someip;


import com.bcd.support_parser.Parser;
import com.bcd.support_parser.impl.someip.data.MessageType;
import com.bcd.support_parser.impl.someip.data.Packet;
import com.bcd.support_parser.impl.someip.data.ReturnCode;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ParserUtil;
import com.bcd.support_parser.util.PerformanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Parser_someip extends Parser {

    static Logger logger= LoggerFactory.getLogger(Parser_someip.class);

    public Parser_someip(boolean printStack) {
        super(printStack);
    }

    public static void main(String[] args) {
        Packet sample = new Packet();
        sample.setServiceId(1);
        sample.setFlag((byte) 1);
        sample.setMethodIdOrEventId((short) 100);
        sample.setClientId(9);
        sample.setSessionId(10);
        sample.setProtocolVersion((short) 3);
        sample.setInterfaceVersion((short) 4);
        sample.setMessageType(MessageType.REQUEST);
        sample.setReturnCode(ReturnCode.E_OK);
        sample.setOffset(10);
        sample.setPayload(new byte[sample.getOffset() * 16]);
        sample.setLength(12 + sample.getPayload().length);



        Parser_someip parser=new Parser_someip(false);
        parser.init();

        String hex = parser.toHex(sample);

        int threadNum=1;
        if(args.length>=1){
            threadNum=Integer.parseInt(args[0]);
        }
        logger.info("param threadNum[{}]",threadNum);
        int num=1000000000;

        PerformanceUtil.testMultiThreadPerformance(hex,parser,Packet.class,threadNum,num,true);
    }

    @Override
    protected List<Class> getParsableClass() {
        return ParserUtil.getParsableClassByScanPackage("com.bcd.parser.impl.someip");
    }

    @Override
    protected List<FieldProcessor> initExtProcessor() {
        return super.initProcessorByScanPackage("com.bcd.parser.impl.someip");
    }
}

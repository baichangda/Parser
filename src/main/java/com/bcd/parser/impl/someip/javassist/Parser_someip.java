package com.bcd.parser.impl.someip.javassist;


import com.bcd.parser.impl.someip.data.Packet;
import com.bcd.parser.javassist.Parser;
import com.bcd.parser.javassist.util.PerformanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser_someip extends Parser {

    static Logger logger= LoggerFactory.getLogger(Parser_someip.class);

    public Parser_someip() {

    }

    public static void main(String[] args) {
        Parser_someip parser=new Parser_someip();
        parser.init();

        String hex = "000100e4000000ac0009000a0304000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a1";

        int threadNum=1;
        if(args.length>=1){
            threadNum=Integer.parseInt(args[0]);
        }
        logger.info("param threadNum[{}]",threadNum);
        int num=1000000000;

        PerformanceUtil.testMultiThreadPerformance(hex,parser,Packet.class,threadNum,num);
    }
}

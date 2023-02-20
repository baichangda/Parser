package com.bcd.support_parser.impl.someip;


import com.bcd.support_parser.impl.someip.data.Packet;
import com.bcd.support_parser.Parser;
import com.bcd.support_parser.util.PerformanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser_someip extends Parser {

    static Logger logger= LoggerFactory.getLogger(Parser_someip.class);

    public Parser_someip() {
    }

    public static void main(String[] args) {

        String hex = "000100e4000000ac0009000a0304000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a1";

        int threadNum=1;
        if(args.length>=1){
            threadNum=Integer.parseInt(args[0]);
        }
        logger.info("param threadNum[{}]",threadNum);
        int num=1000000000;

        PerformanceUtil.testMultiThreadPerformance(hex,Packet.class,threadNum,num,false);


    }
}

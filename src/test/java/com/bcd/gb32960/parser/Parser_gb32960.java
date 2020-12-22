package com.bcd.gb32960.parser;

import com.bcd.gb32960.data.Packet;
import com.bcd.parser.Parser;
import com.bcd.parser.process.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.PerformanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        PerformanceUtil.testSingleThreadPerformance(data,parser,Packet.class,3000000);
//        PerformanceUtil.testPerformance(data,parser,Packet.class,2,10000000);
    }





}

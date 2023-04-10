package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.*;
import com.bcd.support_parser.impl.gb32960.builder.PacketDataFieldBuilder;
import com.bcd.support_parser.impl.gb32960.processor.PacketDataFieldProcessor;



public class Packet {
    //头 0-2
    @F_integer_array(len = 2, singleLen = 1)
    public byte[] header;
    //命令标识 2-3
    @F_integer(len = 1)
    public short flag;
    //应答标识 3-4
    @F_integer(len = 1)
    public short replyFlag;
    //唯一识别码 4-21
    @F_string(len = 17)
    public String vin;
    //数据单元加密方式 21-22
    @F_integer(len = 1)
    public short encodeWay;
    //数据单元长度 22-24
    @F_integer(len = 2, var = 'a')
    public int contentLength;
    //数据单元
//    @F_integer_array(lenExpr = "a", singleLen = 1)
    public byte[] dataContent;
    @F_userDefine(
            processorClass = PacketDataFieldProcessor.class
//            builderClass = PacketDataFieldBuilder.class
    )
//    @F_skip(lenExpr = "a")
    public PacketData data;
    //异或校验位
    @F_integer(len = 1)
    public byte code;
}

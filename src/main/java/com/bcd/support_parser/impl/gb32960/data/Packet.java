package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;
import com.bcd.support_parser.impl.gb32960.processor.PacketDataFieldProcessor;


@Parsable
public class Packet {
    //头 0-2
    @PacketField(index = 1,len = 2)
    public byte[] header;
    //命令标识 2-3
    @PacketField(index = 2,len = 1)
    public short flag;
    //应答标识 3-4
    @PacketField(index = 3,len = 1)
    public short replyFlag;
    //唯一识别码 4-21
    @PacketField(index = 4,len = 17)
    public String vin;
    //数据单元加密方式 21-22
    @PacketField(index = 5,len = 1)
    public short encodeWay;
    //数据单元长度 22-24
    @PacketField(index = 6,len = 2,var = 'a')
    public int contentLength;
    //数据单元
//    @PacketField(index = 7,lenExpr = "a")
    public byte[] dataContent;
    @PacketField(index = 7,lenExpr = "a", processorClass = PacketDataFieldProcessor.class)
    public PacketData data;
    //异或校验位
    @PacketField(index = 8,len = 1)
    public byte code;
}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import com.bcd.parser.impl.gb32960.processor.PacketDataFieldProcessor;


@Parsable
public class Packet {
    //头 0-2
    @PacketField(index = 1,len = 2)
    byte[] header;
    //命令标识 2-3
    @PacketField(index = 2,len = 1)
    short flag;
    //应答标识 3-4
    @PacketField(index = 3,len = 1)
    short replyFlag;
    //唯一识别码 4-21
    @PacketField(index = 4,len = 17)
    String vin;
    //数据单元加密方式 21-22
    @PacketField(index = 5,len = 1)
    short encodeWay;
    //数据单元长度 22-24
    @PacketField(index = 6,len = 2,var = 'a')
    int contentLength;
    //数据单元
//    @PacketField(index = 7,lenExpr = "a")
    byte[] dataContent;
    @PacketField(index = 7,lenExpr = "a", processorClass = PacketDataFieldProcessor.class)
    PacketData data;
    //异或校验位
    @PacketField(index = 8,len = 1)
    byte code;

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }

    public short getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(short replyFlag) {
        this.replyFlag = replyFlag;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public short getEncodeWay() {
        return encodeWay;
    }

    public void setEncodeWay(short encodeWay) {
        this.encodeWay = encodeWay;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getDataContent() {
        return dataContent;
    }

    public void setDataContent(byte[] dataContent) {
        this.dataContent = dataContent;
    }

    public PacketData getData() {
        return data;
    }

    public void setData(PacketData data) {
        this.data = data;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }
}

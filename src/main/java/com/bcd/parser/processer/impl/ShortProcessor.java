package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Objects;

/**
 * 解析short、Short类型字段
 */
public class ShortProcessor extends FieldProcessor<Short> {
    private final static int BYTE_LENGTH=2;

    @Override
    public Short process(ByteBuf data, FieldProcessContext processContext) {
        short res;
        int len=processContext.getLen();
        if(len==1){
            //优化处理 byte->short
            res=data.readUnsignedByte();
        }else {
            if (len == BYTE_LENGTH) {
                res=data.readShort();
            } else if (len > BYTE_LENGTH) {
                data.skipBytes(len - BYTE_LENGTH);
                res=data.readShort();
            } else {
                ByteBuf temp= Unpooled.buffer(BYTE_LENGTH,BYTE_LENGTH);
                temp.writeBytes(new byte[BYTE_LENGTH-len]);
                temp.writeBytes(data,len);
                res=temp.readShort();
            }
        }
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr==null){
            return res;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_short(res,len)){
                return (short)RpnUtil.calc(valExpr,res,-1);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        short newData;
        if(valExpr==null){
            newData=data;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_short(data,processContext.getLen())){
                newData = (short) RpnUtil.deCalc(valExpr,data,-1);
            }else {
                newData=data;
            }
        }
        int len=processContext.getLen();
        //优化处理
        if(len==1){
            dest.writeByte((byte)newData);
        }else if(len==BYTE_LENGTH){
            dest.writeShort(newData);
        }else{
            dest.writeBytes(new byte[len-BYTE_LENGTH]);
            dest.writeShort(newData);
        }

    }

}

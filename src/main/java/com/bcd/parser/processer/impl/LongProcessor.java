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
 * 解析long、Long类型字段
 */
public class LongProcessor extends FieldProcessor<Long> {
    public final static int BYTE_LENGTH=8;

    @Override
    public Long process(ByteBuf data, FieldProcessContext processContext){
        long res;
        int len=processContext.getLen();
        if(len==4){
            //优化处理 int->long
            res=data.readUnsignedInt();
        }else {
            if (len == BYTE_LENGTH) {
                res=data.readLong();
            } else if (len > BYTE_LENGTH) {
                data.skipBytes(len - BYTE_LENGTH);
                res=data.readLong();
            } else {
                ByteBuf temp= Unpooled.buffer(BYTE_LENGTH,BYTE_LENGTH);
                temp.writeBytes(new byte[BYTE_LENGTH-len]);
                temp.writeBytes(data,len);
                res=temp.readLong();
            }
        }
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr==null){
            return res;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_long(res,len)){
                return (long)RpnUtil.calc(valExpr,res,-1);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Long data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        long newData;
        if(valExpr==null){
            newData=data;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_long(data,processContext.getLen())){
                newData = (long) RpnUtil.deCalc(valExpr,data,-1);
            }else {
                newData=data;
            }
        }
        int len=processContext.getLen();
        //优化处理
        if(len==4){
            dest.writeInt((int)newData);
        }else if (len == BYTE_LENGTH) {
            dest.writeLong(newData);
        } else if (len > BYTE_LENGTH) {
            dest.writeBytes(new byte[len - BYTE_LENGTH]);
            dest.writeLong(newData);
        } else {
            for (int i = len; i >= 1; i--) {
                int move = 8 * (i - 1);
                dest.writeByte((byte) (newData >>> move));
            }
        }

    }


}

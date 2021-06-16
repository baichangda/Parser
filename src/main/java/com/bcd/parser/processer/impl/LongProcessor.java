package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

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
        if (len==4){
            res = data.readUnsignedInt();
        }else if(len==BYTE_LENGTH){
            res = data.readLong();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr==null){
            return res;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_long(res,len)){
                return (long)RpnUtil.calc_0(valExpr,res);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Long data, ByteBuf dest, FieldDeProcessContext processContext) {
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        long newData;
        if(valExpr==null){
            newData=data;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_long(data,processContext.getLen())){
                newData = (long) RpnUtil.deCalc_0(valExpr,data);
            }else {
                newData=data;
            }
        }
        int len=processContext.getLen();
        if (len==4){
            dest.writeInt((int)newData);
        }else if(len==BYTE_LENGTH){
            dest.writeLong(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }


}

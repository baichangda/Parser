package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

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
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(res,len)){
            return res;
        }else{
            return RpnUtil.calc_long(valExpr,res);
        }
    }

    @Override
    public void deProcess(Long data, ByteBuf dest, FieldDeProcessContext processContext) {
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        long newData;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(data,processContext.getLen())){
            newData=data;
        }else{
            newData = RpnUtil.deCalc_long(valExpr,data);
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

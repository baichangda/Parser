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

    @Override
    public Long process(ByteBuf data, FieldProcessContext processContext){
        long res;
        int len=processContext.len;
        if (len==4){
            res = data.readUnsignedInt();
        }else if(len==8){
            res = data.readLong();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(res,len)){
            return res;
        }else{
            return RpnUtil.calc_long(valExpr,res);
        }
    }

    @Override
    public void deProcess(Long data, ByteBuf dest, FieldDeProcessContext processContext) {
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        long newData;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_long(data,processContext.len)){
            newData=data;
        }else{
            newData = RpnUtil.deCalc_long(valExpr,data);
        }
        int len=processContext.len;
        if (len==4){
            dest.writeInt((int)newData);
        }else if(len==8){
            dest.writeLong(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }


}

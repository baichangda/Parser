package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ExprCase;
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
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        if(valExprCase==null||!ParserUtil.checkInvalidOrExceptionVal_long(res,len)){
            return res;
        }else{
            return valExprCase.calc_long(res);
        }
    }

    @Override
    public void deProcess(Long data, ByteBuf dest, FieldDeProcessContext processContext) {
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        long newData;
        if(valExprCase==null||!ParserUtil.checkInvalidOrExceptionVal_long(data,processContext.len)){
            newData=data;
        }else{
            newData = valExprCase.deCalc_long(data);
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

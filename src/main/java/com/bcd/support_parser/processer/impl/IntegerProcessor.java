package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ExprCase;
import com.bcd.support_parser.util.ParserUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析int、Integer类型字段
 */
public class IntegerProcessor extends FieldProcessor<Integer> {

    @Override
    public Integer process(ByteBuf data, FieldProcessContext processContext) {
        int res;
        int len = processContext.len;
        if (len==2){
            res = data.readUnsignedShort();
        }else if(len==4){
            res = data.readInt();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        if (valExprCase == null||!ParserUtil.checkInvalidOrExceptionVal_int(res, len)) {
            return res;
        } else {
            return valExprCase.calc_int(res);
        }
    }

    @Override
    public void deProcess(Integer data, ByteBuf dest, FieldDeProcessContext processContext) {
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        int newData;
        if (valExprCase == null||!ParserUtil.checkInvalidOrExceptionVal_int(data, processContext.len)) {
            newData = data;
        } else {
            newData = valExprCase.deCalc_int(data);
        }
        int len = processContext.len;
        if (len==2){
            dest.writeShort((short) newData);
        }else if(len==4){
            dest.writeInt(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

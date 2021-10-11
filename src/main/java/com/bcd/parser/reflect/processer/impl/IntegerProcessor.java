package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
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
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        if (valExpr == null||!ParserUtil.checkInvalidOrExceptionVal_int(res, len)) {
            return res;
        } else {
            return RpnUtil.calc_int(valExpr, res);
        }
    }

    @Override
    public void deProcess(Integer data, ByteBuf dest, FieldDeProcessContext processContext) {
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        int newData;
        if (valExpr == null||!ParserUtil.checkInvalidOrExceptionVal_int(data, processContext.len)) {
            newData = data;
        } else {
            newData = RpnUtil.deCalc_int(valExpr, data);
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

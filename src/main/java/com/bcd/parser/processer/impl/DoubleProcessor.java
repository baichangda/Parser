package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析double、Double类型字段
 * 读取为long类型再转换为double
 */
public class DoubleProcessor extends FieldProcessor<Double> {

    @Override
    public Double process(ByteBuf data, FieldProcessContext processContext) {
        long res;
        int len = processContext.len;
        if(len==4){
            res = data.readUnsignedInt();
        }else if(len==8){
            res = data.readLong();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        if (valExpr == null||!ParserUtil.checkInvalidOrExceptionVal_long(res, len)) {
            return (double) res;
        } else {
            return RpnUtil.calc_double(valExpr, res);
        }
    }

    @Override
    public void deProcess(Double data, ByteBuf dest, FieldDeProcessContext processContext) {
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        long newData;
        //值表达式处理
        if (valExpr == null||!ParserUtil.checkInvalidOrExceptionVal_long(data.longValue(), processContext.len)) {
            newData = data.longValue();
        } else {
            newData = (long) RpnUtil.deCalc_double(valExpr, data);
        }
        //写入原始值
        int len = processContext.len;
        if(len==4){
            dest.writeInt((int) newData);
        }else if(len==8){
            dest.writeLong(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }
}

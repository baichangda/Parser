package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ExprCase;
import com.bcd.support_parser.util.ParserUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析float、Float类型字段
 * 读取为int类型再转换为float
 */
public class FloatProcessor extends FieldProcessor<Float> {

    @Override
    public Float process(ByteBuf data, FieldProcessContext processContext) {
        int res;
        //读取原始值
        int len = processContext.len;
        if (len == 2) {
            res = data.readUnsignedShort();
        } else if (len == 4) {
            res = data.readInt();
        } else {
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_int(res, len)) {
            return (float) res;
        } else {
            final int valPrecision = processContext.fieldInfo.packetField_valPrecision;
            return valExprCase.calc_float(res,valPrecision);
        }
    }

    @Override
    public void deProcess(Float data, ByteBuf dest, FieldDeProcessContext processContext) {
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        int newData;
        if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_int(data.intValue(), processContext.len)) {
            newData = data.intValue();
        } else {
            newData = valExprCase.deCalc_float(data);
        }
        int len = processContext.len;
        if (len == 2) {
            dest.writeShort((short) newData);
        } else if (len == 4) {
            dest.writeInt(newData);
        } else {
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

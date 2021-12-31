package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ExprCase;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析short、Short类型字段
 */
public class ShortProcessor extends FieldProcessor<Short> {

    @Override
    public Short process(ByteBuf data, FieldProcessContext processContext) {
        short res;
        int len = processContext.len;
        if (len == 1) {
            res = data.readUnsignedByte();
        } else if (len == 2) {
            res = data.readShort();
        } else {
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_short(res, len)) {
            return res;
        } else {
            return (short) valExprCase.calc_int(res);
        }
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        short newData;
        if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_short(data, processContext.len)) {
            newData = data;
        } else {
            newData = (short) valExprCase.deCalc_int(data);
        }
        int len = processContext.len;
        if (len == 1) {
            dest.writeByte((byte) newData);
        } else if (len == 2) {
            dest.writeShort(newData);
        } else {
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

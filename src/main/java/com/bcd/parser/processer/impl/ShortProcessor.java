package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析short、Short类型字段
 */
public class ShortProcessor extends FieldProcessor<Short> {
    private final static int BYTE_LENGTH = 2;

    @Override
    public Short process(ByteBuf data, FieldProcessContext processContext) {
        short res;
        int len = processContext.getLen();
        if (len==1){
            res = data.readUnsignedByte();
        }else if(len==BYTE_LENGTH){
            res = data.readShort();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if (valExpr == null) {
            return res;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_short(res, len)) {
                return (short) RpnUtil.calc_0(valExpr, res);
            } else {
                return res;
            }
        }
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        short newData;
        if (valExpr == null) {
            newData = data;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_short(data, processContext.getLen())) {
                newData = (short) RpnUtil.deCalc_0(valExpr, data);
            } else {
                newData = data;
            }
        }
        int len = processContext.getLen();
        if (len==1){
            dest.writeByte((byte) newData);
        }else if(len==BYTE_LENGTH){
            dest.writeShort(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

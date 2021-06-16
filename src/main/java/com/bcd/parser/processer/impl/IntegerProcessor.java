package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析int、Integer类型字段
 */
public class IntegerProcessor extends FieldProcessor<Integer> {
    public final static int BYTE_LENGTH = 4;

    @Override
    public Integer process(ByteBuf data, FieldProcessContext processContext) {
        int res;
        int len = processContext.getLen();
        if (len==2){
            res = data.readUnsignedShort();
        }else if(len==BYTE_LENGTH){
            res = data.readInt();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if (valExpr == null) {
            return res;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_int(res, len)) {
                return (int) RpnUtil.calc_0(valExpr, res);
            } else {
                return res;
            }
        }
    }

    @Override
    public void deProcess(Integer data, ByteBuf dest, FieldDeProcessContext processContext) {
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int newData;
        if (valExpr == null) {
            newData = data;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_int(data, processContext.getLen())) {
                newData = (int) RpnUtil.deCalc_0(valExpr, data);
            } else {
                newData = data;
            }
        }
        int len = processContext.getLen();
        if (len==2){
            dest.writeShort((short) newData);
        }else if(len==BYTE_LENGTH){
            dest.writeInt(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

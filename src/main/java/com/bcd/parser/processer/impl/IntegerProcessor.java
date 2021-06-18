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
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        if (valExpr == null) {
            return res;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_int(res, len)) {
                return RpnUtil.calc_int(valExpr, res);
            } else {
                return res;
            }
        }
    }

    @Override
    public void deProcess(Integer data, ByteBuf dest, FieldDeProcessContext processContext) {
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        int newData;
        if (valExpr == null) {
            newData = data;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_int(data, processContext.getLen())) {
                newData = RpnUtil.deCalc_int(valExpr, data);
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

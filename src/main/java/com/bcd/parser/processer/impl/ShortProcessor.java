package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

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
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        if (valExpr == null) {
            return res;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_short(res, len)) {
                return RpnUtil.calc_short(valExpr, res);
            } else {
                return res;
            }
        }
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        short newData;
        if (valExpr == null) {
            newData = data;
        } else {
            if (ParserUtil.checkInvalidOrExceptionVal_short(data, processContext.getLen())) {
                newData = RpnUtil.deCalc_short(valExpr, data);
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

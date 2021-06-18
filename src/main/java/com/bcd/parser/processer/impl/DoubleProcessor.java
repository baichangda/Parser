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
    public final static int BYTE_LENGTH = 8;

    @Override
    public Double process(ByteBuf data, FieldProcessContext processContext) {
        long res;
        int len = processContext.getLen();
        if(len==4){
            res = data.readUnsignedInt();
        }else if(len==BYTE_LENGTH){
            res = data.readLong();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr_double();
        if (valExpr == null) {
            return (double) res;
        } else {
            //验证异常、无效值
            if (ParserUtil.checkInvalidOrExceptionVal_long(res, len)) {
                return RpnUtil.calc_double(valExpr, res, processContext.getFieldInfo().getValPrecision());
            } else {
                return (double) res;
            }
        }
    }

    @Override
    public void deProcess(Double data, ByteBuf dest, FieldDeProcessContext processContext) {
        double[] valExpr = processContext.getFieldInfo().getValExpr_double();
        long newData;
        //值表达式处理
        if (valExpr == null) {
            newData = data.longValue();
        } else {
            //验证异常、无效值
            if (ParserUtil.checkInvalidOrExceptionVal_long(data.longValue(), processContext.getLen())) {
                newData = (long) RpnUtil.deCalc_double_0(valExpr, data);
            } else {
                newData = data.longValue();
            }
        }
        //写入原始值
        int len = processContext.getLen();
        if(len==4){
            dest.writeInt((int) newData);
        }else if(len==BYTE_LENGTH){
            dest.writeLong(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }
}

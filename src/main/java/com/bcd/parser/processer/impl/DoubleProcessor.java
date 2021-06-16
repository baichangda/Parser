package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

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
        switch (len) {
            case 4: {
                res = data.readUnsignedInt();
                break;
            }
            case BYTE_LENGTH: {
                res = data.readLong();
                break;
            }
            default: {
                throw ParserUtil.newLenNotSupportException(processContext);
            }
        }
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if (valExpr == null) {
            return (double) res;
        } else {
            //验证异常、无效值
            if (ParserUtil.checkInvalidOrExceptionVal_long(res, len)) {
                return RpnUtil.calc(valExpr, res, processContext.getFieldInfo().getValPrecision());
            } else {
                return (double) res;
            }
        }
    }

    @Override
    public void deProcess(Double data, ByteBuf dest, FieldDeProcessContext processContext) {
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        long newData;
        //值表达式处理
        if (valExpr == null) {
            newData = data.longValue();
        } else {
            //验证异常、无效值
            if (ParserUtil.checkInvalidOrExceptionVal_long(data.longValue(), processContext.getLen())) {
                newData = (long) RpnUtil.deCalc_0(valExpr, data);
            } else {
                newData = data.longValue();
            }
        }
        //写入原始值
        int len = processContext.getLen();
        //优化处理
        switch (len) {
            case 4: {
                dest.writeInt((int) newData);
                return;
            }
            case BYTE_LENGTH: {
                dest.writeLong(newData);
                return;
            }
            default: {
                throw ParserUtil.newLenNotSupportException(processContext);
            }
        }
    }
}

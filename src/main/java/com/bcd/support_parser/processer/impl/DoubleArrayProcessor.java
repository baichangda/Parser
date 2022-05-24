package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ExprCase;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析double[]类型字段
 * 读取为long类型再转换为double
 */
public class DoubleArrayProcessor extends FieldProcessor<double[]> {

    @Override
    public double[] process(ByteBuf data, FieldProcessContext processContext) {
        final int len = processContext.len;
        if (len == 0) {
            return new double[0];
        }
        final int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        final int valPrecision = processContext.fieldInfo.packetField_valPrecision;
        //优化处理 int->long
        if (singleLen == 4) {
            final double[] res = new double[len >> 2];
            for (int i = 0; i < res.length; i++) {
                final long cur = data.readUnsignedInt();
                //验证异常、无效值
                if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_long(cur, singleLen)) {
                    res[i] = (double) cur;
                } else {

                    res[i] = valExprCase.calc_double(cur, valPrecision);
                }
            }
            return res;
        } else if (singleLen == 8) {
            final double[] res = new double[len >>> 3];
            for (int i = 0; i < res.length; i++) {
                final long cur = data.readLong();
                //验证异常、无效值
                if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_long(cur, singleLen)) {
                    res[i] = (double) cur;
                } else {
                    res[i] = valExprCase.calc_double(cur, valPrecision);
                }
            }
            return res;
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(double[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        final int len = data.length;
        if (len == 0) {
            return;
        }
        final int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        if (valExprCase == null) {
            if (singleLen == 4) {
                for (double num : data) {
                    dest.writeInt((int) num);
                }
            } else if (singleLen == 8) {
                for (double num : data) {
                    dest.writeLong((long) num);
                }
            } else {
                throw ParserUtil.newSingleLenNotSupportException(processContext);
            }
        } else {
            for (double v : data) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_long((long) v, singleLen)) {
                    if (singleLen == 4) {
                        dest.writeInt((int) valExprCase.deCalc_double(v));
                    } else if (singleLen == 8) {
                        dest.writeLong(valExprCase.deCalc_double(v));
                    } else {
                        throw ParserUtil.newSingleLenNotSupportException(processContext);
                    }
                } else {
                    if (singleLen == 4) {
                        dest.writeInt((int) v);
                    } else if (singleLen == 8) {
                        dest.writeLong((long) v);
                    } else {
                        throw ParserUtil.newSingleLenNotSupportException(processContext);
                    }
                }
            }
        }

    }

}

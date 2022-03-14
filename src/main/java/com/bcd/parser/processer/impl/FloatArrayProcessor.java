package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ExprCase;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析float[]类型字段
 * 读取为int类型再转换为float
 */
public class FloatArrayProcessor extends FieldProcessor<float[]> {

    @Override
    public float[] process(ByteBuf data, FieldProcessContext processContext) {
        final int len = processContext.len;
        if (len == 0) {
            return new float[0];
        }
        final int singleLen = processContext.fieldInfo.packetField_singleLen;
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;

        //优化处理 short->int
        if (singleLen == 2) {
            final float[] res = new float[len >>> 1];
            for (int i = 0; i < res.length; i++) {
                final int cur = data.readUnsignedShort();
                //验证异常、无效值
                if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = (float) cur;
                } else {
                    final int valPrecision = processContext.fieldInfo.packetField_valPrecision;
                    res[i] = valExprCase.calc_float(cur, valPrecision);
                }
            }
            return res;
        } else if (singleLen == 4) {
            float[] res = new float[len >>> 2];
            for (int i = 0; i < res.length; i++) {
                final int cur = data.readInt();
                //验证异常、无效值
                if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = (float) cur;
                } else {
                    final int valPrecision = processContext.fieldInfo.packetField_valPrecision;
                    res[i] = valExprCase.calc_float(cur, valPrecision);
                }
            }
            return res;
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(float[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        final int len = data.length;
        if (len == 0) {
            return;
        }
        final int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;

        if (valExprCase == null) {
            if (singleLen == 2) {
                for (float num : data) {
                    dest.writeShort((short) num);
                }
            } else if (singleLen == 4) {
                for (float num : data) {
                    dest.writeInt((int) num);
                }
            } else {
                throw ParserUtil.newSingleLenNotSupportException(processContext);
            }
        } else {
            for (float v : data) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_long((long) v, singleLen)) {
                    if (singleLen == 2) {
                        dest.writeShort((short) valExprCase.deCalc_double(v));
                    } else if (singleLen == 4) {
                        dest.writeInt(valExprCase.deCalc_float(v));
                    } else {
                        throw ParserUtil.newSingleLenNotSupportException(processContext);
                    }
                } else {
                    if (singleLen == 2) {
                        dest.writeShort((short) v);
                    } else if (singleLen == 4) {
                        dest.writeInt((int) v);
                    } else {
                        throw ParserUtil.newSingleLenNotSupportException(processContext);
                    }
                }
            }
        }
    }

}

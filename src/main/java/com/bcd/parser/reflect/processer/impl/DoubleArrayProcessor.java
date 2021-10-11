package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
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
        int len = processContext.len;
        if (len == 0) {
            return new double[0];
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        //优化处理 int->long
        if (singleLen == 4) {
            double[] res = new double[len >> 2];
            for (int i = 0; i < res.length; i++) {
                long cur = data.readUnsignedInt();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_long(cur, singleLen)) {
                    res[i] = (double) cur;
                } else {
                    res[i] = RpnUtil.calc_double(valExpr, res[i]);
                }
            }
            return res;
        } else if (singleLen == 8) {
            double[] res = new double[len >>> 3];
            for (int i = 0; i < res.length; i++) {
                long cur = data.readLong();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_long(cur, singleLen)) {
                    res[i] = (double) cur;
                } else {
                    res[i] = RpnUtil.calc_double(valExpr, res[i]);
                }
            }
            return res;
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(double[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        double[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new double[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_long((long) data[i], singleLen)) {
                    newData[i] = RpnUtil.deCalc_double(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }
        if (singleLen == 4) {
            for (double num : newData) {
                dest.writeInt((int) num);
            }
        } else if (singleLen == 8) {
            for (double num : newData) {
                dest.writeLong((long) num);
            }
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
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
        int len = processContext.len;
        if (len == 0) {
            return new float[0];
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        //优化处理 short->int
        if (singleLen == 2) {
            float[] res = new float[len >>> 1];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readUnsignedShort();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = (float) cur;
                } else {
                    res[i] = RpnUtil.calc_float(valExpr, res[i]);
                }
            }
            return res;
        } else if (singleLen == 4) {
            float[] res = new float[len >>> 2];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readInt();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = (float) cur;
                } else {
                    res[i] = RpnUtil.calc_float(valExpr, res[i]);
                }
            }
            return res;
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(float[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        float[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new float[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_int((int) data[i], singleLen)) {
                    newData[i] = (float) RpnUtil.deCalc_float(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }

        if (singleLen == 2) {
            for (float num : newData) {
                dest.writeShort((short) num);
            }
        } else if (singleLen == 4) {
            for (float num : newData) {
                dest.writeInt((int) num);
            }
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

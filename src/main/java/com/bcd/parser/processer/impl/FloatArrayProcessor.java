package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析float[]类型字段
 * 读取为int类型再转换为float
 */
public class FloatArrayProcessor extends FieldProcessor<float[]> {
    private final static int BYTE_LENGTH = 4;

    @Override
    public float[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.getLen();
        if (len == 0) {
            return new float[0];
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int valPrecision = processContext.getFieldInfo().getValPrecision();
        //优化处理 short->int
        if(singleLen==2){
            float[] res = new float[len / 2];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readUnsignedShort();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = (float) cur;
                } else {
                    res[i] = (float) RpnUtil.calc(valExpr, res[i], valPrecision);
                }
            }
            return res;
        }else if(singleLen==BYTE_LENGTH){
            float[] res = new float[len / BYTE_LENGTH];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readInt();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = (float) cur;
                } else {
                    res[i] = (float) RpnUtil.calc(valExpr, res[i], valPrecision);
                }
            }
            return res;
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(float[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        float[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new float[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_int((int) data[i], singleLen)) {
                    newData[i] = (float) RpnUtil.deCalc_0(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }

        if(singleLen==2){
            for (float num : newData) {
                dest.writeShort((short) num);
            }
        }else if(singleLen==BYTE_LENGTH){
            for (float num : newData) {
                dest.writeInt((int) num);
            }
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

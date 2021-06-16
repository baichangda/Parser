package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析double[]类型字段
 * 读取为long类型再转换为double
 */
public class DoubleArrayProcessor extends FieldProcessor<double[]> {
    public final static int BYTE_LENGTH = 8;

    @Override
    public double[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.getLen();
        if (len == 0) {
            return new double[0];
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int valPrecision = processContext.getFieldInfo().getValPrecision();
        //优化处理 int->long
        if(singleLen==4){
            double[] res = new double[len / 4];
            for (int i = 0; i < res.length; i++) {
                long cur = data.readUnsignedInt();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_long(cur, singleLen)) {
                    res[i] = (double) cur;
                } else {
                    res[i] = RpnUtil.calc(valExpr, res[i], valPrecision);
                }
            }
            return res;
        }else if(singleLen==BYTE_LENGTH){
            double[] res = new double[len / BYTE_LENGTH];
            for (int i = 0; i < res.length; i++) {
                long cur = data.readLong();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_long(cur, singleLen)) {
                    res[i] = (double) cur;
                } else {
                    res[i] = RpnUtil.calc(valExpr, res[i], valPrecision);
                }
            }
            return res;
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(double[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        double[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new double[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_long((long) data[i], singleLen)) {
                    newData[i] = RpnUtil.deCalc_0(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }
        if(singleLen==4){
            for (double num : newData) {
                dest.writeInt((int) num);
            }
        }else if(singleLen==BYTE_LENGTH){
            for (double num : newData) {
                dest.writeLong((long) num);
            }
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

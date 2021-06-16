package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析int[]类型字段
 */
public class IntegerArrayProcessor extends FieldProcessor<int[]> {
    private final static int BYTE_LENGTH = 4;

    @Override
    public int[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.getLen();
        if (len == 0) {
            return new int[0];
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        //优化处理 short->int
        if(singleLen==2){
            int[] res = new int[len / 2];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readUnsignedShort();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = cur;
                } else {
                    res[i] = (int) RpnUtil.calc_0(valExpr, res[i]);
                }
            }
            return res;
        }else if(singleLen==BYTE_LENGTH){
            int[] res = new int[len / BYTE_LENGTH];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readInt();
                //验证异常、无效值
                if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = cur;
                } else {
                    res[i] = (int) RpnUtil.calc_0(valExpr, res[i]);
                }
            }
            return res;
        }else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

    @Override
    public void deProcess(int[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new int[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_int(data[i], singleLen)) {
                    newData[i] = (int) RpnUtil.deCalc_0(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }
        if(singleLen==2){
            for (long num : newData) {
                dest.writeShort((short) num);
            }
        }else if(singleLen==BYTE_LENGTH){
            for (int num : newData) {
                dest.writeInt(num);
            }
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

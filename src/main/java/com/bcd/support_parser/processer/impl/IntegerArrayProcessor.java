package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ExprCase;
import com.bcd.support_parser.util.ParserUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析int[]类型字段
 */
public class IntegerArrayProcessor extends FieldProcessor<int[]> {

    @Override
    public int[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.len;
        if (len == 0) {
            return new int[0];
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        //优化处理 short->int
        if(singleLen==2){
            int[] res = new int[len >>>1];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readUnsignedShort();
                //验证异常、无效值
                if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = cur;
                } else {
                    res[i] = valExprCase.calc_int(cur);
                }
            }
            return res;
        }else if(singleLen==4){
            int[] res = new int[len >>>2];
            for (int i = 0; i < res.length; i++) {
                int cur = data.readInt();
                //验证异常、无效值
                if (valExprCase == null || !ParserUtil.checkInvalidOrExceptionVal_int(cur, singleLen)) {
                    res[i] = cur;
                } else {
                    res[i] = valExprCase.calc_int(cur);
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
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        int[] newData;
        if (valExprCase == null) {
            newData = data;
        } else {
            newData = new int[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_int(data[i], singleLen)) {
                    newData[i] = valExprCase.deCalc_int(data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }
        if(singleLen==2){
            for (long num : newData) {
                dest.writeShort((short) num);
            }
        }else if(singleLen==4){
            for (int num : newData) {
                dest.writeInt(num);
            }
        }else{
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ExprCase;
import com.bcd.support_parser.util.ParserUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析byte[]类型字段
 */
public class ByteArrayProcessor extends FieldProcessor<byte[]> {

    @Override
    public byte[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.len;
        if (len == 0) {
            return new byte[0];
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        //读取原始值
        if (singleLen == 1) {
            final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
            byte[] res = new byte[len];
            data.readBytes(res);
            if (valExprCase != null) {
                for (int i = 0; i < len; i++) {
                    if(ParserUtil.checkInvalidOrExceptionVal_byte(res[i])){
                        res[i] = (byte) valExprCase.calc_int(res[i]);
                    }
                }
            }
            return res;
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }

    }

    @Override
    public void deProcess(byte[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.fieldInfo.packetField_singleLen;
        //值表达式处理
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        byte[] newData;
        if (valExprCase == null) {
            newData = data;
        } else {
            newData = new byte[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_byte(data[i])) {
                    newData[i] = (byte) valExprCase.deCalc_int(data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }
        //写入原始值
        if (singleLen == 1) {
            dest.writeBytes(newData);
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

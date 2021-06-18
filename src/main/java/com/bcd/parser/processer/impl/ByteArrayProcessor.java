package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析byte[]类型字段
 */
public class ByteArrayProcessor extends FieldProcessor<byte[]> {
    private final static int BYTE_LENGTH = 1;

    @Override
    public byte[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.getLen();
        if (len == 0) {
            return new byte[0];
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        //读取原始值
        if (singleLen == BYTE_LENGTH) {
            int[] valExpr = processContext.getFieldInfo().getValExpr_int();
            byte[] res = new byte[len];
            data.readBytes(res);
            if (valExpr != null) {
                for (int i = 0; i < len; i++) {
                    if(ParserUtil.checkInvalidOrExceptionVal_byte(res[i])){
                        res[i] = RpnUtil.calc_byte(valExpr, res[i]);
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
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        int[] valExpr = processContext.getFieldInfo().getValExpr_int();
        byte[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new byte[len];
            for (int i = 0; i < len; i++) {
                //验证异常、无效值
                if (ParserUtil.checkInvalidOrExceptionVal_byte(data[i])) {
                    newData[i] = RpnUtil.deCalc_byte(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }
        //写入原始值
        if (singleLen == BYTE_LENGTH) {
            dest.writeBytes(newData);
        } else {
            throw ParserUtil.newSingleLenNotSupportException(processContext);
        }
    }

}

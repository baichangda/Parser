package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析short[]类型字段
 */
public class ShortArrayProcessor extends FieldProcessor<short[]> {
    private final static int BYTE_LENGTH = 2;

    @Override
    public short[] process(ByteBuf data, FieldProcessContext processContext) {
        int len = processContext.getLen();
        if (len == 0) {
            return new short[0];
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        //优化处理 byte->short
        switch (singleLen) {
            case 1: {
                short[] res = new short[len];
                for (int i = 0; i < len; i++) {
                    short cur = data.readUnsignedByte();
                    if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_short(cur, singleLen)) {
                        res[i] = cur;
                    } else {
                        res[i] = (short) RpnUtil.calc_0(valExpr, cur);
                    }
                }
                return res;
            }
            case BYTE_LENGTH: {
                short[] res = new short[len / BYTE_LENGTH];
                for (int i = 0; i < res.length; i++) {
                    short cur = data.readShort();
                    //验证异常、无效值
                    if (valExpr == null || !ParserUtil.checkInvalidOrExceptionVal_short(cur, singleLen)) {
                        res[i] = cur;
                    } else {
                        res[i] = (short) RpnUtil.calc_0(valExpr, cur);
                    }
                }
                return res;
            }
            default: {
                throw ParserUtil.newSingleLenNotSupportException(processContext);
            }
        }
    }

    @Override
    public void deProcess(short[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int len = data.length;
        if (len == 0) {
            return;
        }
        int singleLen = processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        short[] newData;
        if (valExpr == null) {
            newData = data;
        } else {
            newData = new short[len];
            for (int i = 0; i < len; i++) {
                if (ParserUtil.checkInvalidOrExceptionVal_short(data[i], singleLen)) {
                    newData[i] = (short) RpnUtil.deCalc_0(valExpr, data[i]);
                } else {
                    newData[i] = data[i];
                }
            }
        }

        //优化处理
        switch (singleLen) {
            case 1: {
                for (short num : newData) {
                    dest.writeByte((byte) num);
                }
                return;
            }
            case BYTE_LENGTH: {
                for (short num : newData) {
                    dest.writeShort(num);
                }
                return;
            }
            default: {
                throw ParserUtil.newSingleLenNotSupportException(processContext);
            }
        }
    }
}

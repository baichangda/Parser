package com.bcd.parser.processer.impl;

import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Objects;

/**
 * 解析double、Double类型字段
 * 读取为long类型再转换为double
 */
public class DoubleProcessor extends FieldProcessor<Double> {
    public final static int BYTE_LENGTH=8;

    @Override
    public Double process(ByteBuf data, FieldProcessContext processContext) {
        long res;
        int len=processContext.getLen();
        if(len==4){
            //优化处理 int->long
            res= data.readUnsignedInt();
        }else {
            if (len == BYTE_LENGTH) {
                res=data.readLong();
            } else if (len > BYTE_LENGTH) {
                data.skipBytes(len - BYTE_LENGTH);
                res=data.readLong();
            } else {
                ByteBuf temp= Unpooled.buffer(BYTE_LENGTH,BYTE_LENGTH);
                temp.writeBytes(new byte[BYTE_LENGTH-len]);
                temp.writeBytes(data,len);
                res=temp.readLong();
            }
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn==null){
            return (double)res;
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_long(res,len)){
                return RpnUtil.calcRPN_char_double_singleVar(valRpn,res,processContext.getFieldInfo().getValPrecision());
            }else{
                return (double)res;
            }
        }
    }

    @Override
    public void deProcess(Double data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        long newData;
        //值表达式处理
        if(reverseValRpn==null){
            newData=data.longValue();
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_long(data.longValue(),processContext.getLen())){
                newData = (long) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data,0);
            }else {
                newData=data.longValue();
            }
        }
        //写入原始值
        int len=processContext.getLen();
        //优化处理
        if(len==4){
            dest.writeInt((int)newData);
        }else if (len == BYTE_LENGTH) {
            dest.writeLong(newData);
        } else if (len > BYTE_LENGTH) {
            dest.writeBytes(new byte[len - BYTE_LENGTH]);
            dest.writeLong(newData);
        } else {
            for (int i = len; i >= 1; i--) {
                int move = 8 * (i - 1);
                dest.writeByte((byte) (newData >>> move));
            }
        }
    }
}

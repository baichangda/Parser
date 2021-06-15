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
 * 解析int、Integer类型字段
 */
public class IntegerProcessor extends FieldProcessor<Integer> {
    public final static int BYTE_LENGTH=4;

    @Override
    public Integer process(ByteBuf data, FieldProcessContext processContext){
        int res;
        int len=processContext.getLen();
        if(len==2){
            //优化处理 short->int
            res=data.readUnsignedShort();
        }else {
            if (len == BYTE_LENGTH) {
                res=data.readInt();
            } else if (len > BYTE_LENGTH) {
                data.skipBytes(len - BYTE_LENGTH);
                res=data.readInt();
            } else {
                ByteBuf temp= Unpooled.buffer(BYTE_LENGTH,BYTE_LENGTH);
                temp.writeBytes(new byte[BYTE_LENGTH-len]);
                temp.writeBytes(data,len);
                res=temp.readInt();
            }
        }
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn==null){
            return res;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_int(res,len)){
                return (int) RpnUtil.calcRPN_char_double_singleVar(valRpn,res,-1);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Integer data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        int newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_int(data,processContext.getLen())){
                newData = (int) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data,-1);
            }else {
                newData=data;
            }
        }
        int len=processContext.getLen();
        //优化处理
        if(len==2) {
            dest.writeShort((short) newData);
        }else if (len == BYTE_LENGTH) {
            dest.writeInt(newData);
        } else if (len > BYTE_LENGTH) {
            dest.writeBytes(new byte[len - BYTE_LENGTH]);
            dest.writeInt(newData);
        } else {
            for (int i = len; i >= 1; i--) {
                int move = 8 * (i - 1);
                dest.writeByte((byte) (newData >>> move));
            }
        }

    }

}

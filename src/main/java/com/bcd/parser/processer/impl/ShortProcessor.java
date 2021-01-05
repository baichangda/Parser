package com.bcd.parser.processer.impl;

import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Objects;

/**
 * 解析short、Short类型字段
 */
public class ShortProcessor extends FieldProcessor<Short> {
    private final static int BYTE_LENGTH=2;

    @Override
    public Short process(ByteBuf data, FieldProcessContext processContext) {
        short res;
        int len=processContext.getLen();
        if(len==1){
            //优化处理 byte->short
            res=data.readUnsignedByte();
        }else {
            if (len == BYTE_LENGTH) {
                res=data.readShort();
            } else if (len > BYTE_LENGTH) {
                data.skipBytes(len - BYTE_LENGTH);
                res=data.readShort();
            } else {
                ByteBuf temp= Unpooled.buffer(BYTE_LENGTH,BYTE_LENGTH);
                temp.writeBytes(new byte[BYTE_LENGTH-len]);
                temp.writeBytes(data,len);
                res=temp.readShort();
            }
        }
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn==null){
            return res;
        }else{
            if(checkInvalidOrExceptionVal(res,len)){
                return (short)RpnUtil.calcRPN_char_double_singleVar(valRpn,res);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        short newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            if(checkInvalidOrExceptionVal(data,processContext.getFieldInfo().getPacketField_singleLen())){
                newData = (short) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data);
            }else {
                newData=data;
            }
        }
        int len=processContext.getLen();
        if(len==BYTE_LENGTH){
            dest.writeShort(newData);
        }else if(len>BYTE_LENGTH){
            dest.writeBytes(new byte[len-BYTE_LENGTH]);
            dest.writeShort(newData);
        }else{
            for(int i=len;i>=1;i--){
                int move=8*(i-1);
                dest.writeByte((byte)(newData>>>move));
            }
        }
    }

    public boolean checkInvalidOrExceptionVal(short val,int len){
        switch (len) {
            case 1: {
                return val != 0xff && val != 0xfe;
            }
            case 2: {
                return val != (short)0xffff && val != (short)0xfffe;
            }
            default: {
                throw BaseRuntimeException.getException("param len[{0}] not support", len);
            }
        }
    }
}

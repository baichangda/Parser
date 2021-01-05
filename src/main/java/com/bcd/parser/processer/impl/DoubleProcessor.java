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
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn==null){
            return (double)res;
        }else{
            if(checkInvalidOrExceptionVal(res,len)){
                return RpnUtil.calcRPN_char_double_singleVar(valRpn,res);
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
        if(reverseValRpn==null){
            newData=data.longValue();
        }else{
            if(checkInvalidOrExceptionVal(data.longValue(),processContext.getFieldInfo().getPacketField_singleLen())){
                newData = (long) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data);
            }else {
                newData=data.longValue();
            }
        }
        int len=processContext.getLen();
        if(len==BYTE_LENGTH){
            dest.writeLong(newData);
        }else if(len>BYTE_LENGTH){
            dest.writeBytes(new byte[len-BYTE_LENGTH]);
            dest.writeLong(newData);
        }else{
            for(int i=len;i>=1;i--){
                int move=8*(i-1);
                dest.writeByte((byte)(newData>>>move));
            }
        }
    }

    public boolean checkInvalidOrExceptionVal(long val,int len){
        switch (len) {
            case 1: {
                return val != 0xff && val != 0xfe;
            }
            case 2: {
                return val != 0xffff && val != 0xfffe;
            }
            case 3: {
                return val != 0xffffff && val != 0xfffffe;
            }
            case 4: {
                return val != 0xffffffff && val != 0xfffffffe;
            }
            case 5: {
                return val != 0xffffffffffL && val != 0xfffffffffeL;
            }
            case 6: {
                return val != 0xffffffffffffL && val != 0xfffffffffffeL;
            }
            case 7: {
                return val != 0xffffffffffffffL && val != 0xfffffffffffffeL;
            }
            case 8: {
                return val != 0xffffffffffffffffL && val != 0xfffffffffffffffeL;
            }
            default: {
                throw BaseRuntimeException.getException("param len[{0}] not support", len);
            }
        }
    }
}
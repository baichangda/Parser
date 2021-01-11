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
 * 解析long、Long类型字段
 */
public class LongProcessor extends FieldProcessor<Long> {
    public final static int BYTE_LENGTH=8;

    @Override
    public Long process(ByteBuf data, FieldProcessContext processContext){
        long res;
        int len=processContext.getLen();
        if(len==4){
            //优化处理 int->long
            res=data.readUnsignedInt();
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
            return res;
        }else{
            if(checkInvalidOrExceptionVal(res,len)){
                return (long)RpnUtil.calcRPN_char_double_singleVar(valRpn,res,-1);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Long data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        long newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            if(checkInvalidOrExceptionVal(data,processContext.getLen())){
                newData = (long) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data,-1);
            }else {
                newData=data;
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

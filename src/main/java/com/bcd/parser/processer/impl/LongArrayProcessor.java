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
 * 解析long[]类型字段
 */
public class LongArrayProcessor extends FieldProcessor<long[]> {
    public final static int BYTE_LENGTH=8;

    @Override
    public long[] process(ByteBuf data, FieldProcessContext processContext){
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        int len= processContext.getLen();
        long[] res=new long[len/singleLen];
        //优化处理 int->long
        if(singleLen==4){
            for(int i=0;i<res.length;i++){
                res[i]=data.readUnsignedInt();
            }
        }else{
            if(singleLen==BYTE_LENGTH){
                for(int i=0;i<res.length;i++){
                    res[i]=data.readLong();
                }
            }else if(singleLen>BYTE_LENGTH){
                int diff=singleLen-BYTE_LENGTH;
                for(int i=0;i<res.length;i++){
                    data.skipBytes(diff);
                    res[i]=data.readLong();
                }
            }else{
                ByteBuf temp= Unpooled.buffer(len,len);
                int diff=BYTE_LENGTH-singleLen;
                for(int i=0;i<res.length;i++){
                    temp.writeBytes(new byte[diff]);
                    temp.writeBytes(data,BYTE_LENGTH);
                    res[i]=temp.readLong();
                    temp.clear();
                }
            }
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn!=null){
            for(int i=0;i<res.length-1;i++){
                if(checkInvalidOrExceptionVal(res[i],singleLen)){
                    res[i] = (long) RpnUtil.calcRPN_char_double_singleVar(valRpn, res[i]);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(long[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        if(singleLen==BYTE_LENGTH){
            for (long num : data) {
                dest.writeLong(num);
            }
        }else if(singleLen>BYTE_LENGTH){
            for (long num : data) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeLong(num);
            }
        }else{
            for (long num : data) {
                for(int i=singleLen;i>=1;i--){
                    int move=8*(i-1);
                    dest.writeByte((byte)(num>>>move));
                }
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

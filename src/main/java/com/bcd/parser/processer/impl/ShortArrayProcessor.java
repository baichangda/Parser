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
 * 解析short[]类型字段
 */
public class ShortArrayProcessor extends FieldProcessor<short[]> {
    private final static int BYTE_LENGTH=2;
    @Override
    public short[] process(ByteBuf data, FieldProcessContext processContext) {
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        int len=processContext.getLen();
        short[] res;
        //优化处理 byte->short
        if(singleLen==1){
            res=new short[len];
            for(int i=0;i<res.length;i++){
                res[i]=data.readUnsignedByte();
            }
        }else{
            res=new short[len/singleLen];
            if(singleLen==BYTE_LENGTH){
                for(int i=0;i<res.length;i++){
                    res[i]=data.readShort();
                }
            }else if(singleLen>BYTE_LENGTH){
                int diff=singleLen-BYTE_LENGTH;
                for(int i=0;i<res.length;i++){
                    data.skipBytes(diff);
                    res[i]=data.readShort();
                }
            }else{
                ByteBuf temp= Unpooled.buffer(len,len);
                int diff=BYTE_LENGTH-singleLen;
                for(int i=0;i<res.length;i++){
                    temp.writeBytes(new byte[diff]);
                    temp.writeBytes(data,singleLen);
                    res[i]=temp.readShort();
                    temp.clear();
                }
            }
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn!=null){
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(checkInvalidOrExceptionVal(res[i],singleLen)){
                    res[i]=(short) RpnUtil.calcRPN_char_double_singleVar(valRpn,res[i],-1);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(short[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();

        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        short[] newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            newData=new short[data.length];
            for(int i=0;i<data.length;i++){
                if(checkInvalidOrExceptionVal(data[i],singleLen)){
                    newData[i]=(short) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn,data[i],-1);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        if(singleLen==BYTE_LENGTH){
            for (short num : newData) {
                dest.writeShort(num);
            }
        }else if(singleLen>BYTE_LENGTH){
            for (short num : newData) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeShort(num);
            }
        }else{
            for (short num : newData) {
                for(int i=singleLen;i>=1;i--){
                    int move=8*(i-1);
                    dest.writeByte((byte)(num>>>move));
                }
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

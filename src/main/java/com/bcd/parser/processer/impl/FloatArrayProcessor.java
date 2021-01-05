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
 * 解析float[]类型字段
 */
public class FloatArrayProcessor extends FieldProcessor<float[]> {
    private final static int BYTE_LENGTH=4;

    @Override
    public float[] process(ByteBuf data, FieldProcessContext processContext){
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        int len =processContext.getLen();
        int[] res=new int[len/singleLen];
        //优化处理 short->int
        if(singleLen==2){
            for(int i=0;i<res.length;i++){
                res[i]=data.readUnsignedShort();
            }
        }else{
            if(singleLen==BYTE_LENGTH){
                for(int i=0;i<res.length;i++){
                    res[i]=data.readInt();
                }
            }else if(singleLen>BYTE_LENGTH){
                int diff=singleLen-BYTE_LENGTH;
                for(int i=0;i<res.length;i++){
                    data.skipBytes(diff);
                    res[i]=data.readInt();
                }
            }else{
                ByteBuf temp= Unpooled.buffer(len,len);
                int diff=BYTE_LENGTH-singleLen;
                for(int i=0;i<res.length;i++){
                    temp.writeBytes(new byte[diff]);
                    temp.writeBytes(data,BYTE_LENGTH);
                    res[i]=temp.readInt();
                    temp.clear();
                }
            }
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        float[] finalRes=new float[res.length];
        if(valRpn==null){
            for(int i=0;i<res.length-1;i++){
                finalRes[i]=res[i];
            }
        }else{
            for(int i=0;i<res.length-1;i++){
                if(checkInvalidOrExceptionVal(res[i],singleLen)){
                    finalRes[i]=(float) RpnUtil.calcRPN_char_double_singleVar(valRpn,res[i]);
                }else{
                    finalRes[i]=res[i];
                }
            }
        }
        return finalRes;
    }

    @Override
    public void deProcess(float[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        if(singleLen==BYTE_LENGTH){
            for (float num : data) {
                dest.writeInt((int)num);
            }
        }else if(singleLen>BYTE_LENGTH){
            for (float num : data) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeInt((int)num);
            }
        }else{
            for (float num : data) {
                for(int i=singleLen;i>=1;i--){
                    int move=8*(i-1);
                    dest.writeByte((byte)((int)num>>>move));
                }
            }
        }
    }

    public boolean checkInvalidOrExceptionVal(int val,int len){
        switch (len){
            case 1:{
                return val != 0xff && val != 0xfe;
            }
            case 2:{
                return val != 0xffff && val != 0xfffe;
            }
            case 3:{
                return val != 0xffffff && val != 0xfffffe;
            }
            case 4:{
                return val != 0xffffffff && val != 0xfffffffe;
            }
            default:{
                throw BaseRuntimeException.getException("param len[{0}] not support",len);
            }
        }
    }
}

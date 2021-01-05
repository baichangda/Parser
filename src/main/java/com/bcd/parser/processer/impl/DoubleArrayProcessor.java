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
 * 解析double[]类型字段
 * 读取为long类型再转换为double
 */
public class DoubleArrayProcessor extends FieldProcessor<double[]> {
    public final static int BYTE_LENGTH=8;

    @Override
    public double[] process(ByteBuf data, FieldProcessContext processContext){
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
                    temp.writeBytes(data,singleLen);
                    res[i]=temp.readLong();
                    temp.clear();
                }
            }
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        double[] finalRes=new double[res.length];
        if(valRpn==null){
            for(int i=0;i<res.length-1;i++){
                finalRes[i]=res[i];
            }
        }else{
            for(int i=0;i<res.length-1;i++){
                //验证异常、无效值
                if(checkInvalidOrExceptionVal(res[i],singleLen)){
                    finalRes[i]=RpnUtil.calcRPN_char_double_singleVar(valRpn,res[i]);
                }else{
                    finalRes[i]=res[i];
                }
            }
        }
        return finalRes;
    }

    @Override
    public void deProcess(double[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();

        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        double[] newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            newData=new double[data.length];
            for(int i=0;i<data.length;i++){
                if(checkInvalidOrExceptionVal((long)data[i],singleLen)){
                    newData[i]= RpnUtil.calcRPN_char_double_singleVar(reverseValRpn,data[i]);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        if(singleLen==BYTE_LENGTH){
            for (double num : newData) {
                dest.writeLong((long)num);
            }
        }else if(singleLen>BYTE_LENGTH){
            for (double num : newData) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeLong((long)num);
            }
        }else{
            for (double num : newData) {
                for(int i=singleLen;i>=1;i--){
                    int move=8*(i-1);
                    dest.writeByte((byte)((long)num>>>move));
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

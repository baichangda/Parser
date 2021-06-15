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
            for(int i=0;i<res.length;i++){
                finalRes[i]=res[i];
            }
        }else{
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_long(res[i],singleLen)){
                    finalRes[i]=RpnUtil.calcRPN_char_double_singleVar(valRpn,res[i],processContext.getFieldInfo().getValPrecision());
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
        //值表达式处理
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        double[] newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            newData=new double[data.length];
            for(int i=0;i<data.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_long((long)data[i],singleLen)){
                    newData[i]= RpnUtil.calcRPN_char_double_singleVar(reverseValRpn,data[i],0);
                }else{
                    newData[i]=data[i];
                }
            }
        }
        //写入原始值
        //优化处理
        if(singleLen==4){
            for (double num : newData) {
                dest.writeInt((int)num);
            }
        }else if (singleLen == BYTE_LENGTH) {
            for (double num : newData) {
                dest.writeLong((long) num);
            }
        } else if (singleLen > BYTE_LENGTH) {
            for (double num : newData) {
                dest.writeBytes(new byte[singleLen - BYTE_LENGTH]);
                dest.writeLong((long) num);
            }
        } else {
            for (double num : newData) {
                for (int i = singleLen; i >= 1; i--) {
                    int move = 8 * (i - 1);
                    dest.writeByte((byte) ((long) num >>> move));
                }
            }
        }
    }

}

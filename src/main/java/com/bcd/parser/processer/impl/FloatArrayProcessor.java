package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Objects;

/**
 * 解析float[]类型字段
 * 读取为int类型再转换为float
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
                    temp.writeBytes(data,singleLen);
                    res[i]=temp.readInt();
                    temp.clear();
                }
            }
        }
        //值表达式处理
        float[] finalRes=new float[res.length];
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr==null){
            for(int i=0;i<res.length;i++){
                finalRes[i]=res[i];
            }
        }else{
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_int(res[i],singleLen)){
                    finalRes[i]=(float) RpnUtil.calc(valExpr,res[i],processContext.getFieldInfo().getValPrecision());
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
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        float[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new float[data.length];
            for(int i=0;i<data.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_int((int)data[i],singleLen)){
                    newData[i]=(float) RpnUtil.deCalc(valExpr,data[i],0);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        //写入原始值
        //优化处理
        if(singleLen==2){
            for (float num : newData) {
                dest.writeShort((short)num);
            }
        }else if (singleLen == BYTE_LENGTH) {
            for (float num : newData) {
                dest.writeInt((int) num);
            }
        } else if (singleLen > BYTE_LENGTH) {
            for (float num : newData) {
                dest.writeBytes(new byte[singleLen - BYTE_LENGTH]);
                dest.writeInt((int) num);
            }
        } else {
            for (float num : newData) {
                for (int i = singleLen; i >= 1; i--) {
                    int move = 8 * (i - 1);
                    dest.writeByte((byte) ((int) num >>> move));
                }
            }
        }
    }

}

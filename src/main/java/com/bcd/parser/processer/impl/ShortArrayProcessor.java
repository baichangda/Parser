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
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr!=null){
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_short(res[i],singleLen)){
                    res[i]=(short) RpnUtil.calc(valExpr,res[i],-1);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(short[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();

        double[] valExpr = processContext.getFieldInfo().getValExpr();
        short[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new short[data.length];
            for(int i=0;i<data.length;i++){
                if(ParserUtil.checkInvalidOrExceptionVal_short(data[i], singleLen)){
                    newData[i]=(short) RpnUtil.deCalc(valExpr,data[i],0);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        //优化处理
        if(singleLen==1){
            for (short num : newData) {
                dest.writeByte((byte)num);
            }
        }else if (singleLen == BYTE_LENGTH) {
            for (short num : newData) {
                dest.writeShort(num);
            }
        } else{
            for (short num : newData) {
                dest.writeBytes(new byte[singleLen - BYTE_LENGTH]);
                dest.writeShort(num);
            }
        }

    }
}

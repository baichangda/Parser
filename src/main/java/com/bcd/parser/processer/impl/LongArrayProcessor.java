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
 * 解析long[]类型字段
 */
public class LongArrayProcessor extends FieldProcessor<long[]> {
    public final static int BYTE_LENGTH=8;

    @Override
    public long[] process(ByteBuf data, FieldProcessContext processContext){
        int len =processContext.getLen();
        if(len==0){
            return new long[0];
        }
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
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
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr!=null){
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_long(res[i],singleLen)){
                    res[i] = (long) RpnUtil.calc( valExpr,res[i],-1);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(long[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int len = data.length;
        if(len ==0){
            return;
        }
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        long[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new long[len];
            for(int i = 0; i< len; i++){
                if(ParserUtil.checkInvalidOrExceptionVal_long(data[i],singleLen)){
                    newData[i]=(long) RpnUtil.deCalc(valExpr,data[i],0);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        //优化处理
        if(singleLen==4){
            for (long num : newData) {
                dest.writeInt((int)num);
            }
        }else if (singleLen == BYTE_LENGTH) {
            for (long num : newData) {
                dest.writeLong(num);
            }
        } else if (singleLen > BYTE_LENGTH) {
            for (long num : newData) {
                dest.writeBytes(new byte[singleLen - BYTE_LENGTH]);
                dest.writeLong(num);
            }
        } else {
            for (long num : newData) {
                for (int i = singleLen; i >= 1; i--) {
                    int move = 8 * (i - 1);
                    dest.writeByte((byte) (num >>> move));
                }
            }
        }

    }

}

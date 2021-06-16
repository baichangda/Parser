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
 * 解析int[]类型字段
 */
public class IntegerArrayProcessor extends FieldProcessor<int[]> {
    private final static int BYTE_LENGTH=4;

    @Override
    public int[] process(ByteBuf data, FieldProcessContext processContext){
        //读取原始值
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
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr!=null){
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_int(res[i],singleLen)){
                    res[i]=(int) RpnUtil.calc(valExpr,res[i],-1);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(int[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new int[data.length];
            for(int i=0;i<data.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_int(data[i],singleLen)){
                    newData[i]=(int) RpnUtil.deCalc(valExpr,data[i],-1);
                }else{
                    newData[i]=data[i];
                }
            }
        }
        //写入原始值
        //优化处理
        if(singleLen==2){
            for (long num : newData) {
                dest.writeShort((short)num);
            }
        }else if (singleLen == BYTE_LENGTH) {
            for (int num : newData) {
                dest.writeInt(num);
            }
        } else if (singleLen > BYTE_LENGTH) {
            for (int num : newData) {
                dest.writeBytes(new byte[singleLen - BYTE_LENGTH]);
                dest.writeInt(num);
            }
        } else {
            for (int num : newData) {
                for (int i = singleLen; i >= 1; i--) {
                    int move = 8 * (i - 1);
                    dest.writeByte((byte) (num >>> move));
                }
            }
        }
    }

}

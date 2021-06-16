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
 * 解析float、Float类型字段
 * 读取为int类型再转换为float
 */
public class FloatProcessor extends FieldProcessor<Float> {
    public final static int BYTE_LENGTH=4;

    @Override
    public Float process(ByteBuf data, FieldProcessContext processContext) {
        int res;
        //读取原始值
        int len=processContext.getLen();
        if(len==2){
            //优化处理 short->int
            res=data.readUnsignedShort();
        }else {
            if (len == BYTE_LENGTH) {
                res=data.readInt();
            } else if (len > BYTE_LENGTH) {
                data.skipBytes(len - BYTE_LENGTH);
                res=data.readInt();
            } else {
                ByteBuf temp= Unpooled.buffer(BYTE_LENGTH,BYTE_LENGTH);
                temp.writeBytes(new byte[BYTE_LENGTH-len]);
                temp.writeBytes(data,len);
                res=temp.readInt();
            }
        }
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr==null){
            return (float)res;
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_int(res,len)){
                return (float) RpnUtil.calc(valExpr,res,processContext.getFieldInfo().getValPrecision());
            }else{
                return (float)res;
            }
        }
    }

    @Override
    public void deProcess(Float data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int newData;
        if(valExpr==null){
            newData=data.intValue();
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_int(data.intValue(),processContext.getLen())){
                newData = (int) RpnUtil.deCalc(valExpr,data,-1);
            }else {
                newData=data.intValue();
            }
        }
        int len=processContext.getLen();
        //优化处理
        if(len==2){
            dest.writeShort((short)newData);
        }else if (len == BYTE_LENGTH) {
            dest.writeInt(newData);
        } else if (len > BYTE_LENGTH) {
            dest.writeBytes(new byte[len - BYTE_LENGTH]);
            dest.writeInt(newData);
        } else {
            for (int i = len; i >= 1; i--) {
                int move = 8 * (i - 1);
                dest.writeByte((byte) (newData >>> move));
            }
        }
    }

}

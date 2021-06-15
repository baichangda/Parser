package com.bcd.parser.processer.impl;


import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析byte、Byte类型字段
 */
public class ByteProcessor extends FieldProcessor<Byte> {
    private final static int BYTE_LENGTH=1;

    @Override
    public Byte process(ByteBuf data, FieldProcessContext processContext) {
        //读取原始值
        int len=processContext.getLen();
        byte res;
        if(len==BYTE_LENGTH){
            res=data.readByte();
        }else if(len>BYTE_LENGTH){
            data.skipBytes(len-BYTE_LENGTH);
            res=data.readByte();
        }else{
            res=0;
        }
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr==null){
            return res;
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_byte(res)){
                return (byte)RpnUtil.calc(valExpr,res,-1);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Byte data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        byte newData;
        if(valExpr==null){
            newData=data;
        }else{
            if(ParserUtil.checkInvalidOrExceptionVal_byte(data)){
                newData=data;
            }else {
                newData = (byte) RpnUtil.deCalc(valExpr,data,0);
            }
        }
        int len=processContext.getLen();
        if(len==BYTE_LENGTH){
            dest.writeByte(data);
        }else {
            byte[] content = new byte[len];
            content[len - BYTE_LENGTH] = newData;
            dest.writeBytes(content);
        }
    }
}

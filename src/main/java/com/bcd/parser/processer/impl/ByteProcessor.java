package com.bcd.parser.processer.impl;


import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析byte、Byte类型字段
 */
public class ByteProcessor extends FieldProcessor<Byte> {

    @Override
    public Byte process(ByteBuf data, FieldProcessContext processContext) {
        //读取原始值
        int len=processContext.len;
        byte res;
        if(len==1){
            res=data.readByte();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_byte(res)){
            return res;
        }else{
            return (byte)RpnUtil.calc_int(valExpr,res);
        }
    }

    @Override
    public void deProcess(Byte data, ByteBuf dest, FieldDeProcessContext processContext) {
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        byte newData;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_byte(data)){
            newData=data;
        }else{
            newData = (byte) RpnUtil.deCalc_int(valExpr,data);
        }
        int len=processContext.len;
        if(len==1){
            dest.writeByte(newData);
        }else {
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }
}

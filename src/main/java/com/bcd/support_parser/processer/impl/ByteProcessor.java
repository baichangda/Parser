package com.bcd.support_parser.processer.impl;


import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import com.bcd.support_parser.util.ExprCase;
import com.bcd.support_parser.util.ParserUtil;
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
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        if(valExprCase==null||!ParserUtil.checkInvalidOrExceptionVal_byte(res)){
            return res;
        }else{
            return (byte) valExprCase.calc_int(res);
        }
    }

    @Override
    public void deProcess(Byte data, ByteBuf dest, FieldDeProcessContext processContext) {
        final ExprCase valExprCase = processContext.fieldInfo.valExprCase;
        byte newData;
        if(valExprCase==null||!ParserUtil.checkInvalidOrExceptionVal_byte(data)){
            newData=data;
        }else{
            newData = (byte) valExprCase.deCalc_int(data);
        }
        int len=processContext.len;
        if(len==1){
            dest.writeByte(newData);
        }else {
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }
}

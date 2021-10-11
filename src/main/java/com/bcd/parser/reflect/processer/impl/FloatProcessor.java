package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解析float、Float类型字段
 * 读取为int类型再转换为float
 */
public class FloatProcessor extends FieldProcessor<Float> {

    @Override
    public Float process(ByteBuf data, FieldProcessContext processContext) {
        int res;
        //读取原始值
        int len=processContext.len;
        if (len==2){
            res = data.readUnsignedShort();
        }else if(len==4){
            res = data.readInt();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_int(res,len)){
            return (float)res;
        }else{
            return RpnUtil.calc_float(valExpr,res);
        }
    }

    @Override
    public void deProcess(Float data, ByteBuf dest, FieldDeProcessContext processContext) {
        //值表达式处理
        int[] valExpr = processContext.fieldInfo.valExpr_int;
        int newData;
        if(valExpr==null||!ParserUtil.checkInvalidOrExceptionVal_int(data.intValue(),processContext.len)){
            newData=data.intValue();
        }else{
            newData = (int) RpnUtil.deCalc_float(valExpr,data);
        }
        int len=processContext.len;
        if (len==2){
            dest.writeShort((short)newData);
        }else if(len==4){
            dest.writeInt(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

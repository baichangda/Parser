package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

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
        switch (len){
            case 2:{
                //优化处理 short->int
                res=data.readUnsignedShort();
                break;
            }
            case BYTE_LENGTH:{
                res=data.readInt();
                break;
            }
            default:{
                throw ParserUtil.newLenNotSupportException(processContext);
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
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        int newData;
        if(valExpr==null){
            newData=data.intValue();
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_int(data.intValue(),processContext.getLen())){
                newData = (int) RpnUtil.deCalc_0(valExpr,data);
            }else {
                newData=data.intValue();
            }
        }
        int len=processContext.getLen();
        switch (len){
            case 2:{
                dest.writeShort((short)newData);
                return;
            }
            case BYTE_LENGTH:{
                dest.writeInt(newData);
                return;
            }
            default:{
                throw ParserUtil.newLenNotSupportException(processContext);
            }
        }
    }

}

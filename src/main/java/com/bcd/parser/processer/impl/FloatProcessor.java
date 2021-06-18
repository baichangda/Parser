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
    public final static int BYTE_LENGTH=4;

    @Override
    public Float process(ByteBuf data, FieldProcessContext processContext) {
        int res;
        //读取原始值
        int len=processContext.getLen();
        if (len==2){
            res = data.readUnsignedShort();
        }else if(len==BYTE_LENGTH){
            res = data.readInt();
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr_double();
        if(valExpr==null){
            return (float)res;
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_int(res,len)){
                return (float) RpnUtil.calc_double(valExpr,res,processContext.getFieldInfo().getValPrecision());
            }else{
                return (float)res;
            }
        }
    }

    @Override
    public void deProcess(Float data, ByteBuf dest, FieldDeProcessContext processContext) {
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr_double();
        int newData;
        if(valExpr==null){
            newData=data.intValue();
        }else{
            //验证异常、无效值
            if(ParserUtil.checkInvalidOrExceptionVal_int(data.intValue(),processContext.getLen())){
                newData = (int) RpnUtil.deCalc_double_0(valExpr,data);
            }else {
                newData=data.intValue();
            }
        }
        int len=processContext.getLen();
        if (len==2){
            dest.writeShort((short)newData);
        }else if(len==BYTE_LENGTH){
            dest.writeInt(newData);
        }else{
            throw ParserUtil.newLenNotSupportException(processContext);
        }
    }

}

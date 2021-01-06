package com.bcd.parser.processer.impl;

import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
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
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn==null){
            return (float)res;
        }else{
            if(checkInvalidOrExceptionVal(res,len)){
                return (float) RpnUtil.calcRPN_char_double_singleVar(valRpn,res,processContext.getFieldInfo().getValExprPrecision());
            }else{
                return (float)res;
            }
        }
    }

    @Override
    public void deProcess(Float data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        int newData;
        if(reverseValRpn==null){
            newData=data.intValue();
        }else{
            if(checkInvalidOrExceptionVal(data.intValue(),processContext.getLen())){
                newData = (int) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data,0);
            }else {
                newData=data.intValue();
            }
        }
        int len=processContext.getLen();
        if(len==BYTE_LENGTH){
            dest.writeInt(newData);
        }else if(len>BYTE_LENGTH){
            dest.writeBytes(new byte[len-BYTE_LENGTH]);
            dest.writeInt(newData);
        }else{
            for(int i=len;i>=1;i--){
                int move=8*(i-1);
                dest.writeByte((byte)(newData>>>move));
            }
        }
    }

    public boolean checkInvalidOrExceptionVal(int val,int len){
        switch (len){
            case 1:{
                return val != 0xff && val != 0xfe;
            }
            case 2:{
                return val != 0xffff && val != 0xfffe;
            }
            case 3:{
                return val != 0xffffff && val != 0xfffffe;
            }
            case 4:{
                return val != 0xffffffff && val != 0xfffffffe;
            }
            default:{
                throw BaseRuntimeException.getException("param len[{0}] not support",len);
            }
        }
    }
}

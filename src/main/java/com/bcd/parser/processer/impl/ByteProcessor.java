package com.bcd.parser.processer.impl;


import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
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
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn==null){
            return res;
        }else{
            if(checkInvalidOrExceptionVal(res)){
                return (byte)RpnUtil.calcRPN_char_double_singleVar(valRpn,res);
            }else{
                return res;
            }
        }
    }

    @Override
    public void deProcess(Byte data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        byte newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            if(checkInvalidOrExceptionVal(data)){
                newData=data;
            }else {
                newData = (byte) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn, data);
            }
        }
        int len=processContext.getLen();
        byte[] content=new byte[len];
        content[len-BYTE_LENGTH]=newData;
        dest.writeBytes(content);
    }

    public boolean checkInvalidOrExceptionVal(byte val){
        return val != (byte) 0xff && val != (byte) 0xfe;
    }
}
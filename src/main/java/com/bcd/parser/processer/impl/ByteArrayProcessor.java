package com.bcd.parser.processer.impl;

import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析byte[]类型字段
 */
public class ByteArrayProcessor extends FieldProcessor<byte[]> {
    private final static int BYTE_LENGTH=1;

    @Override
    public byte[] process(ByteBuf data, FieldProcessContext processContext)  {
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        byte[] res;
        if(singleLen==BYTE_LENGTH){
            res=new byte[processContext.getLen()];
            data.readBytes(res);
        }else if(singleLen>BYTE_LENGTH){
            res=new byte[processContext.getLen()/singleLen];
            int diff=singleLen-BYTE_LENGTH;
            for(int i=0;i<res.length;i++){
                data.skipBytes(diff);
                res[i]=data.readByte();
            }
        }else{
            throw BaseRuntimeException.getException("packetField_singleLen can not less than 1");
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn!=null){
            for(int i=0;i<res.length-1;i++){
                if(checkInvalidOrExceptionVal(res[i])){
                    res[i]=(byte) RpnUtil.calcRPN_char_double_singleVar(valRpn,res[i]);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(byte[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        byte[] newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            newData=new byte[data.length];
            for(int i=0;i<data.length;i++){
                if(checkInvalidOrExceptionVal(data[i])){
                    newData[i]=(byte) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn,data[i]);
                }else{
                    newData[i]=data[i];
                }
            }
        }
        if(singleLen==BYTE_LENGTH){
            dest.writeBytes(newData);
        }else if(singleLen>BYTE_LENGTH){
            for (byte num : newData) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeByte(num);
            }
        }
    }

    public boolean checkInvalidOrExceptionVal(byte val){
        return val != (byte) 0xff && val != (byte) 0xfe;
    }
}

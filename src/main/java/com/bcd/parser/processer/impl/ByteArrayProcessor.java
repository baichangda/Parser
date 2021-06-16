package com.bcd.parser.processer.impl;

import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.util.ParserUtil;
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
        //读取原始值
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
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        if(valExpr!=null){
            for(int i=0;i<res.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_byte(res[i])){
                    res[i]=(byte) RpnUtil.calc(valExpr,res[i],-1);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(byte[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        //值表达式处理
        double[] valExpr = processContext.getFieldInfo().getValExpr();
        byte[] newData;
        if(valExpr==null){
            newData=data;
        }else{
            newData=new byte[data.length];
            for(int i=0;i<data.length;i++){
                //验证异常、无效值
                if(ParserUtil.checkInvalidOrExceptionVal_byte(data[i])){
                    newData[i]=(byte) RpnUtil.deCalc(valExpr,data[i],0);
                }else{
                    newData[i]=data[i];
                }
            }
        }
        //写入原始值
        if(singleLen==BYTE_LENGTH){
            dest.writeBytes(newData);
        }else{
            for (byte num : newData) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeByte(num);
            }
        }
    }

}

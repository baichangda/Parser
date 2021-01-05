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
 * 解析int[]类型字段
 */
public class IntegerArrayProcessor extends FieldProcessor<int[]> {
    private final static int BYTE_LENGTH=4;

    @Override
    public int[] process(ByteBuf data, FieldProcessContext processContext){
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        int len =processContext.getLen();
        int[] res=new int[len/singleLen];
        //优化处理 short->int
        if(singleLen==2){
            for(int i=0;i<res.length;i++){
                res[i]=data.readUnsignedShort();
            }
        }else{
            if(singleLen==BYTE_LENGTH){
                for(int i=0;i<res.length;i++){
                    res[i]=data.readInt();
                }
            }else if(singleLen>BYTE_LENGTH){
                int diff=singleLen-BYTE_LENGTH;
                for(int i=0;i<res.length;i++){
                    data.skipBytes(diff);
                    res[i]=data.readInt();
                }
            }else{
                ByteBuf temp= Unpooled.buffer(len,len);
                int diff=BYTE_LENGTH-singleLen;
                for(int i=0;i<res.length;i++){
                    temp.writeBytes(new byte[diff]);
                    temp.writeBytes(data,singleLen);
                    res[i]=temp.readInt();
                    temp.clear();
                }
            }
        }
        //值表达式处理
        Object[] valRpn=processContext.getFieldInfo().getValRpn();
        if(valRpn!=null){
            for(int i=0;i<res.length-1;i++){
                if(checkInvalidOrExceptionVal(res[i],singleLen)){
                    res[i]=(int) RpnUtil.calcRPN_char_double_singleVar(valRpn,res[i]);
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(int[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();

        Object[] reverseValRpn= processContext.getFieldInfo().getReverseValRpn();
        int[] newData;
        if(reverseValRpn==null){
            newData=data;
        }else{
            newData=new int[data.length];
            for(int i=0;i<data.length;i++){
                if(checkInvalidOrExceptionVal(data[i],singleLen)){
                    newData[i]=(int) RpnUtil.calcRPN_char_double_singleVar(reverseValRpn,data[i]);
                }else{
                    newData[i]=data[i];
                }
            }
        }

        if(singleLen==BYTE_LENGTH){
            for (int num : newData) {
                dest.writeInt(num);
            }
        }else if(singleLen>BYTE_LENGTH){
            for (int num : newData) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeInt(num);
            }
        }else{
            for (int num : newData) {
                for(int i=singleLen;i>=1;i--){
                    int move=8*(i-1);
                    dest.writeByte((byte)(num>>>move));
                }
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

package com.bcd.parser.process.impl;

import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.process.FieldDeProcessContext;
import com.bcd.parser.process.FieldProcessContext;
import com.bcd.parser.process.FieldProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.Objects;

/**
 * 解析long[]类型字段
 */
public class LongArrayProcessor extends FieldProcessor<long[]> {
    public final static int BYTE_LENGTH=8;

    @Override
    public long[] process(ByteBuf data, FieldProcessContext processContext){
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        int len= processContext.getLen();
        long[] res=new long[len/singleLen];
        //优化处理 int->long
        if(singleLen==4){
            for(int i=0;i<res.length;i++){
                res[i]=data.readUnsignedInt();
            }
        }else{
            if(singleLen==BYTE_LENGTH){
                for(int i=0;i<res.length;i++){
                    res[i]=data.readLong();
                }
            }else if(singleLen>BYTE_LENGTH){
                int diff=singleLen-BYTE_LENGTH;
                for(int i=0;i<res.length;i++){
                    data.skipBytes(diff);
                    res[i]=data.readLong();
                }
            }else{
                ByteBuf temp= Unpooled.buffer(len,len);
                int diff=BYTE_LENGTH-singleLen;
                for(int i=0;i<res.length;i++){
                    temp.writeBytes(new byte[diff]);
                    temp.writeBytes(data,BYTE_LENGTH);
                    res[i]=temp.readLong();
                    temp.clear();
                }
            }
        }
        return res;
    }

    @Override
    public void deProcess(long[] data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        int singleLen= processContext.getFieldInfo().getPacketField_singleLen();
        if(singleLen==BYTE_LENGTH){
            for (long num : data) {
                dest.writeLong(num);
            }
        }else if(singleLen>BYTE_LENGTH){
            for (long num : data) {
                dest.writeBytes(new byte[singleLen-BYTE_LENGTH]);
                dest.writeLong(num);
            }
        }else{
            for (long num : data) {
                for(int i=singleLen;i>=1;i--){
                    int move=8*(i-1);
                    dest.writeByte((byte)(num>>>move));
                }
            }
        }
    }
}

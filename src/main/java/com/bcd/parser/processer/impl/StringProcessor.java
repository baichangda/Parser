package com.bcd.parser.processer.impl;

import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * 解析{@link String}类型字段
 * 步骤如下:
 * 1、先读取到定长的byte[]中
 * 2、从尾部检测无效字节长度(byte=0)
 * 3、将有效字节转换为字符串
 *
 */
public class StringProcessor extends FieldProcessor<String> {
    @Override
    public String process(ByteBuf data, FieldProcessContext processContext) {
        int discardLen=0;
        byte[] bytes=new byte[processContext.len];
        data.readBytes(bytes);
        for(int i=bytes.length-1;i>=0;i--){
            if(bytes[i]==0){
                discardLen++;
            }else{
                break;
            }
        }
        return new String(bytes,0,bytes.length-discardLen);
    }

    @Override
    public void deProcess(String data, ByteBuf dest, FieldDeProcessContext processContext) {
        int len=processContext.len;
        byte[] content=data.getBytes();
        if(content.length==len){
            dest.writeBytes(content);
        }else if(content.length<len){
            dest.writeBytes(content);
            dest.writeBytes(new byte[len-content.length]);
        }else{
            throw BaseRuntimeException.getException("toByteBuf error,data byte length["+content.length+"]>len["+len+"]");
        }
    }
}

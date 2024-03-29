package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 解析{@link ByteBuf}类型字段
 */
public class ByteBufProcessor extends FieldProcessor<ByteBuf> {

    @Override
    public ByteBuf process(ByteBuf data, FieldProcessContext processContext) {
        int len=processContext.len;
        ByteBuf byteBuf= Unpooled.buffer(len,len);
        byteBuf.writeBytes(data,len);
        return byteBuf;
    }

    @Override
    public void deProcess(ByteBuf data, ByteBuf dest, FieldDeProcessContext processContext) {
        dest.writeBytes(data);
    }
}

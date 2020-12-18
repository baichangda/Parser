package com.bcd.parser.process.impl;

import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.process.FieldDeProcessContext;
import com.bcd.parser.process.FieldProcessContext;
import com.bcd.parser.process.FieldProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Objects;

/**
 * 解析{@link ByteBuf}类型字段
 */
public class ByteBufProcessor extends FieldProcessor<ByteBuf> {

    @Override
    public ByteBuf process(ByteBuf data, FieldProcessContext processContext) {
        int len=processContext.getLen();
        ByteBuf byteBuf= Unpooled.buffer(len,len);
        byteBuf.writeBytes(data,len);
        return byteBuf;
    }

    @Override
    public void deProcess(ByteBuf data, ByteBuf dest, FieldDeProcessContext processContext) {
        Objects.requireNonNull(data);
        dest.writeBytes(data);
    }
}

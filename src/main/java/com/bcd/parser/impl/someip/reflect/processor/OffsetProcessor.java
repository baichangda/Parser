package com.bcd.parser.impl.someip.reflect.processor;

import com.bcd.parser.reflect.processer.FieldDeProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class OffsetProcessor extends FieldProcessor<Integer> {

    @Override
    public Integer process(ByteBuf data, FieldProcessContext processContext) {
        int i = data.readInt();
        //解析flag
        return i >> 4;
    }

    @Override
    public void deProcess(Integer data, ByteBuf dest, FieldDeProcessContext processContext) {
        dest.writeInt((data << 4) | 0b0001);
    }
}

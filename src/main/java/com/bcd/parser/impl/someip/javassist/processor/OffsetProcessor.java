package com.bcd.parser.impl.someip.javassist.processor;

import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class OffsetProcessor extends FieldProcessor<Integer> {

    @Override
    public Integer process(final ByteBuf data, final FieldProcessContext context) {
        int i = data.readInt();
        //解析flag
        return i >> 4;
    }
}

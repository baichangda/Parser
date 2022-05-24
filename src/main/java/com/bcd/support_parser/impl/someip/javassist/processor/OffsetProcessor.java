package com.bcd.support_parser.impl.someip.javassist.processor;

import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import com.bcd.support_parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class OffsetProcessor extends FieldProcessor<Integer> {

    @Override
    public Integer process(final ByteBuf data, final FieldProcessContext context) {
        //解析flag
        return data.readInt() >> 4;
    }
}

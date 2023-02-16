package com.bcd.support_parser.impl.someip.javassist.processor;

import com.bcd.support_parser.javassist.processor.Processor;
import com.bcd.support_parser.javassist.processor.ProcessContext;
import io.netty.buffer.ByteBuf;

public class OffsetProcessor extends Processor<Integer> {

    @Override
    public Integer process(final ByteBuf data, final ProcessContext parentContext) {
        //解析flag
        return data.readInt() >> 4;
    }
}

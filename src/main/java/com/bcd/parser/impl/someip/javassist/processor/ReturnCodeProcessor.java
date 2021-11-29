package com.bcd.parser.impl.someip.javassist.processor;

import com.bcd.parser.impl.someip.data.ReturnCode;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class ReturnCodeProcessor extends FieldProcessor<ReturnCode> {

    @Override
    public ReturnCode process(final ByteBuf data, final FieldProcessContext context) {
        byte b = data.readByte();
        return ReturnCode.valueOf(b);
    }

}

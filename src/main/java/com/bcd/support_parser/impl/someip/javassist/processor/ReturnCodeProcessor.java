package com.bcd.support_parser.impl.someip.javassist.processor;

import com.bcd.support_parser.impl.someip.data.ReturnCode;
import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import com.bcd.support_parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class ReturnCodeProcessor extends FieldProcessor<ReturnCode> {

    @Override
    public ReturnCode process(final ByteBuf data, final FieldProcessContext context) {
        return ReturnCode.valueOf(data.readByte());
    }

}

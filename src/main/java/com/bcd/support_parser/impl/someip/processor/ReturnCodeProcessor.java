package com.bcd.support_parser.impl.someip.processor;

import com.bcd.support_parser.impl.someip.data.ReturnCode;
import com.bcd.support_parser.processor.Processor;
import com.bcd.support_parser.processor.ProcessContext;
import io.netty.buffer.ByteBuf;

public class ReturnCodeProcessor extends Processor<ReturnCode> {

    @Override
    public ReturnCode process(final ByteBuf data, final ProcessContext parentContext) {
        return ReturnCode.valueOf(data.readByte());
    }
}

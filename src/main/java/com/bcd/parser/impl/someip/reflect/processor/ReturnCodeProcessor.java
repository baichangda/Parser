package com.bcd.parser.impl.someip.processor;

import com.bcd.parser.impl.someip.data.ReturnCode;
import com.bcd.parser.reflect.processer.FieldDeProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class ReturnCodeProcessor extends FieldProcessor<ReturnCode> {

    @Override
    public ReturnCode process(ByteBuf data, FieldProcessContext processContext) {
        byte b = data.readByte();
        return ReturnCode.valueOf(b);
    }

    @Override
    public void deProcess(ReturnCode data, ByteBuf dest, FieldDeProcessContext processContext) {
        dest.writeByte(data.getVal());
    }
}

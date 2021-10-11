package com.bcd.parser.impl.someip.javassist.processor;

import com.bcd.parser.impl.someip.data.Packet;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class MethodIdOrEventIdProcessor extends FieldProcessor<Short> {
    @Override
    public Short process(ByteBuf data, FieldProcessContext context) {
        short s = data.readShort();
        //解析flag
        ((Packet) context.instance).setFlag((byte) ((s >> 7) & 0x01));
        return (short) (s & (0xff >> 1));
    }
}

package com.bcd.support_parser.impl.someip.javassist.processor;

import com.bcd.support_parser.impl.someip.data.Packet;
import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import com.bcd.support_parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class MethodIdOrEventIdProcessor extends FieldProcessor<Short> {
    @Override
    public Short process(final ByteBuf data, final FieldProcessContext context) {
        short s = data.readShort();
        //解析flag
        ((Packet) context.instance).flag=(byte) ((s >> 7) & 0x01);
        return (short) (s & (0xff >> 1));
    }
}

package com.bcd.support_parser.impl.someip.javassist.processor;

import com.bcd.support_parser.impl.someip.data.MessageType;
import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import com.bcd.support_parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class MessageTypeProcessor extends FieldProcessor<MessageType> {
    @Override
    public MessageType process(final ByteBuf data, final FieldProcessContext context) {
        return MessageType.valueOf(data.readByte());
    }
}

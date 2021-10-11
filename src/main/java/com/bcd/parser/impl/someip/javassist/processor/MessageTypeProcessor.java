package com.bcd.parser.impl.someip.javassist.processor;

import com.bcd.parser.impl.someip.data.MessageType;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.processor.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class MessageTypeProcessor extends FieldProcessor<MessageType> {
    @Override
    public MessageType process(ByteBuf data, FieldProcessContext context) {
        byte b = data.readByte();
        return MessageType.valueOf(b);
    }
}

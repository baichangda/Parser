package com.bcd.support_parser.impl.someip.javassist.processor;

import com.bcd.support_parser.impl.someip.data.MessageType;
import com.bcd.support_parser.javassist.processor.Processor;
import com.bcd.support_parser.javassist.processor.ProcessContext;
import io.netty.buffer.ByteBuf;

public class MessageTypeProcessor extends Processor<MessageType> {
    @Override
    public MessageType process(final ByteBuf data, final ProcessContext parentContext) {
        return MessageType.valueOf(data.readByte());
    }

}

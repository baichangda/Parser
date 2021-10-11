package com.bcd.parser.impl.someip.reflect.processor;

import com.bcd.parser.impl.someip.data.MessageType;
import com.bcd.parser.reflect.processer.FieldDeProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

public class MessageTypeProcessor extends FieldProcessor<MessageType> {

    @Override
    public MessageType process(ByteBuf data, FieldProcessContext processContext) {
        byte b = data.readByte();
        return MessageType.valueOf(b);
    }

    @Override
    public void deProcess(MessageType data, ByteBuf dest, FieldDeProcessContext processContext) {
        dest.writeByte(data.getVal());
    }
}

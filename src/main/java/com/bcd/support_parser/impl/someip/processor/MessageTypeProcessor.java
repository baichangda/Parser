package com.bcd.support_parser.impl.someip.processor;

import com.bcd.support_parser.impl.someip.data.MessageType;
import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
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

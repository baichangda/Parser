package com.bcd.support_parser.impl.someip.builder;

import com.bcd.support_parser.impl.someip.data.MessageType;
import com.bcd.support_parser.builder.BuilderContext;
import com.bcd.support_parser.builder.FieldBuilder;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class MessageTypeBuilder extends FieldBuilder{

    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameInstance = context.varNameInstance;
        final String messageTypeClassName = MessageType.class.getName();
        JavassistUtil.append(body, "{}.{}={}.valueOf({}.readByte());\n", varNameInstance, field.getName(),messageTypeClassName, FieldBuilder.varNameByteBuf);
    }
}

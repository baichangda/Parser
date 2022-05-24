package com.bcd.support_parser.impl.someip.javassist.builder;

import com.bcd.support_parser.impl.someip.data.MessageType;
import com.bcd.support_parser.javassist.builder.BuilderContext;
import com.bcd.support_parser.javassist.builder.FieldBuilder;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class MessageTypeBuilder extends FieldBuilder{

    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameInstance = context.varNameInstance;
        final String messageTypeClassName = MessageType.class.getName();
        JavassistUtil.append(body, "{}.{}={}.valueOf({}.readByte());\n", varNameInstance, field.getName(),messageTypeClassName, FieldBuilder.varNameByteBuf);
    }
}

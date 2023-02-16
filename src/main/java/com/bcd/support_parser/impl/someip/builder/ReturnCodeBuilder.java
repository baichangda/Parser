package com.bcd.support_parser.impl.someip.javassist.builder;

import com.bcd.support_parser.impl.someip.data.ReturnCode;
import com.bcd.support_parser.javassist.builder.BuilderContext;
import com.bcd.support_parser.javassist.builder.FieldBuilder;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ReturnCodeBuilder extends FieldBuilder{

    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameInstance = context.varNameInstance;
        final String returnCodeClassName = ReturnCode.class.getName();
        JavassistUtil.append(body, "{}.{}={}.valueOf({}.readByte());\n", varNameInstance, field.getName(),returnCodeClassName, FieldBuilder.varNameByteBuf);
    }
}

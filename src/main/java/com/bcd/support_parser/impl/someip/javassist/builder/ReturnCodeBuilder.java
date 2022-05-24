package com.bcd.parser.impl.someip.javassist.builder;

import com.bcd.parser.impl.someip.data.ReturnCode;
import com.bcd.parser.javassist.builder.BuilderContext;
import com.bcd.parser.javassist.builder.FieldBuilder;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ReturnCodeBuilder extends FieldBuilder{

    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        final String returnCodeClassName = ReturnCode.class.getName();
        JavassistUtil.append(body, "{}.{}({}.valueOf({}.readByte()));\n", varNameInstance, setMethodName,returnCodeClassName, FieldBuilder.varNameByteBuf);
    }
}

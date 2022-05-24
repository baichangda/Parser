package com.bcd.support_parser.impl.someip.javassist.builder;

import com.bcd.support_parser.javassist.builder.BuilderContext;
import com.bcd.support_parser.javassist.builder.FieldBuilder;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class MethodIdOrEventIdBuilder extends FieldBuilder {

    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        JavassistUtil.append(body, "short {} = {}.readShort();\n", varNameField, FieldBuilder.varNameByteBuf);
        JavassistUtil.append(body, "{}.flag=(byte)(({}>>7)&0x01);\n", varNameInstance, varNameField);
        JavassistUtil.append(body, "{}.{}=(short)({}&(0xff>>1));\n", varNameInstance, field.getName(), varNameField);
    }

}

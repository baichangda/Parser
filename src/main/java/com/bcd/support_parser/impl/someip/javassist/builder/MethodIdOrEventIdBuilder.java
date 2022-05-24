package com.bcd.parser.impl.someip.javassist.builder;

import com.bcd.parser.javassist.builder.BuilderContext;
import com.bcd.parser.javassist.builder.FieldBuilder;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class MethodIdOrEventIdBuilder extends FieldBuilder {

    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        JavassistUtil.append(body, "short {} = {}.readShort();\n", varNameField, FieldBuilder.varNameByteBuf);
        JavassistUtil.append(body, "{}.setFlag((byte)(({}>>7)&0x01));\n", varNameInstance, varNameField);
        JavassistUtil.append(body, "{}.{}((short)({}&(0xff>>1)));\n", varNameInstance, setMethodName, varNameField);
    }

}

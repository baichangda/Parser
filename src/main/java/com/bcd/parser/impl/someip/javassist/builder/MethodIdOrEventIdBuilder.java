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
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instanceVarName = context.instanceVarName;
        JavassistUtil.append(body, "short {} = {}.readShort();\n", fieldVarName, byteBuf_var_name);
        JavassistUtil.append(body, "{}.setFlag((byte)(({}>>7)&0x01));\n", instanceVarName, fieldVarName);
        JavassistUtil.append(body, "{}.{}((short)({}&(0xff>>1)));\n", instanceVarName, setMethodName, fieldVarName);
    }

}

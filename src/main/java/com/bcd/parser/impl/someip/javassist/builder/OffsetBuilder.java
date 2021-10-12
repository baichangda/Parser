package com.bcd.parser.impl.someip.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.builder.BuilderContext;
import com.bcd.parser.javassist.builder.FieldBuilder;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class OffsetBuilder extends FieldBuilder{

    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        JavassistUtil.append(body,"{} {}={}.readInt()>>4;\n", fieldTypeClassName,varNameField, FieldBuilder.varNameByteBuf);
        JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
        context.varToFieldName.put(packetField.var(),varNameField);
    }
}

package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ByteFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0') {
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body, "{}.{}({}.readByte());\n", varNameInstance, setMethodName, FieldBuilder.varNameByteBuf);
                    return;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
        }else{
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body,"{} {}={}.readByte();\n",fieldTypeClassName,varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
                    break;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
            context.varToFieldName.put(packetField.var(),varNameField);
        }
    }
}

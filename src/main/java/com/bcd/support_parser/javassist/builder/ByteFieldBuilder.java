package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ByteFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0') {
            if (packetField.len() == 1) {
                JavassistUtil.append(body, "{}.{}={}.readByte();\n", varNameInstance, field.getName(), FieldBuilder.varNameByteBuf);
            } else {
                JavassistUtil.packetFieldLenNotSupport(field);
            }
        }else{
            if (packetField.len() == 1) {
                JavassistUtil.append(body, "final {} {}={}.readByte();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
            } else {
                JavassistUtil.packetFieldLenNotSupport(field);
            }
            context.varToFieldName.put(packetField.var(),varNameField);
        }
    }
}
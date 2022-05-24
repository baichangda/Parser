package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ShortFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0'){
            switch (packetField.len()) {
                case 1 -> {
                    JavassistUtil.append(body, "{}.{}={}.readUnsignedByte();\n", varNameInstance, field.getName(), FieldBuilder.varNameByteBuf);
                }
                case 2 -> {
                    JavassistUtil.append(body, "{}.{}={}.readShort();\n", varNameInstance, field.getName(), FieldBuilder.varNameByteBuf);
                }
                default -> {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
        }else {
            switch (packetField.len()) {
                case 1 -> {
                    JavassistUtil.append(body, "final {} {}={}.readUnsignedByte();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                }
                case 2 -> {
                    JavassistUtil.append(body, "final {} {}={}.readShort();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                }
                default -> {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
            context.varToFieldName.put(packetField.var(),varNameField);
        }
    }
}

package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class IntFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        final int bitLen = packetField.bitLen();
        if (packetField.var() == '0') {
            switch (packetField.len()) {
                case 2: {
                    JavassistUtil.append(body, "{}.{}={}.readUnsignedShort();\n", varNameInstance, field.getName(), FieldBuilder.varNameByteBuf);
                    break;
                }
                case 4: {
                    JavassistUtil.append(body, "{}.{}={}.readInt();\n", varNameInstance, field.getName(), FieldBuilder.varNameByteBuf);
                    break;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                    break;
                }
            }
        } else {
            switch (packetField.len()) {
                case 2: {
                    JavassistUtil.append(body, "final {} {}={}.readUnsignedShort();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                    break;
                }
                case 4: {
                    JavassistUtil.append(body, "final {} {}={}.readInt();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                    break;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                    break;
                }
            }
            context.varToFieldName.put(packetField.var(), varNameField);
        }
    }
}

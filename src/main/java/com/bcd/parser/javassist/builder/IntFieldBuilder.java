package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class IntFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0'){
            switch (packetField.len()) {
                case 2 : {
                    JavassistUtil.append(body, "{}.{}({}.readUnsignedShort());\n", varNameInstance, setMethodName, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 4 : {
                    JavassistUtil.append(body, "{}.{}({}.readInt());\n", varNameInstance, setMethodName, FieldBuilder.varNameByteBuf);
                    break;
                }
                default : {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
        }else {
            switch (packetField.len()) {
                case 2 : {
                    JavassistUtil.append(body, "final {} {}={}.readUnsignedShort();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
                    break;
                }
                case 4 : {
                    JavassistUtil.append(body, "final {} {}={}.readInt();\n", fieldTypeClassName, varNameField, FieldBuilder.varNameByteBuf);
                    JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
                    break;
                }
                default : {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
            context.varToFieldName.put(packetField.var(),varNameField);
        }
    }
}

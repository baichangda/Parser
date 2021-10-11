package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ByteFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instanceVarName = context.instanceVarName;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0') {
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body, "{}.{}({}.readByte());\n", instanceVarName, setMethodName, byteBuf_var_name);
                    return;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
        }else{
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body,"{} {}={}.readByte();\n",fieldTypeClassName,fieldVarName,byteBuf_var_name);
                    JavassistUtil.append(body, "{}.{}({});\n", instanceVarName, setMethodName, fieldVarName);
                    break;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
            context.varToFieldName.put(packetField.var(),fieldVarName);
        }
    }
}

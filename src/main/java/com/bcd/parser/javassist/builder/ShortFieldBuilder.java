package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ShortFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String instanceVarName = context.instanceVarName;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0'){
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body, "{}.{}({}.readUnsignedByte());\n", instanceVarName, setMethodName, byteBuf_var_name);
                    return;
                }
                case 2: {
                    JavassistUtil.append(body, "{}.{}({}.readShort());\n", instanceVarName, setMethodName, byteBuf_var_name);
                    return;
                }
                default: {
                    JavassistUtil.packetFieldLenNotSupport(field);
                }
            }
        }else {
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body,"{} {}={}.readUnsignedByte();\n",fieldTypeClassName,fieldVarName,byteBuf_var_name);
                    JavassistUtil.append(body, "{}.{}({});\n", instanceVarName, setMethodName, fieldVarName);
                    break;
                }
                case 2: {
                    JavassistUtil.append(body,"{} {}={}.readShort();\n",fieldTypeClassName,fieldVarName,byteBuf_var_name);
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

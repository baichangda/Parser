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
        final String instance_var_name = context.instance_var_name;
        final String fieldTypeClassName = field.getType().getName();
        if(packetField.var()=='0'){
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body, "{}.{}({}.readUnsignedByte());\n", instance_var_name, setMethodName, byteBuf_var_name);
                    return;
                }
                case 2: {
                    JavassistUtil.append(body, "{}.{}({}.readShort());\n", instance_var_name, setMethodName, byteBuf_var_name);
                    return;
                }
                default: {
                    JavassistUtil.packetField_len_notSupport(field);
                }
            }
        }else {
            switch (packetField.len()) {
                case 1: {
                    JavassistUtil.append(body,"{} {}={}.readUnsignedByte();\n",fieldTypeClassName,fieldVarName,byteBuf_var_name);
                    JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName, fieldVarName);
                    break;
                }
                case 2: {
                    JavassistUtil.append(body,"{} {}={}.readShort();\n",fieldTypeClassName,fieldVarName,byteBuf_var_name);
                    JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName, fieldVarName);
                    break;
                }
                default: {
                    JavassistUtil.packetField_len_notSupport(field);
                }
            }
            context.var_to_fieldName.put(packetField.var(),fieldVarName);
        }
    }
}

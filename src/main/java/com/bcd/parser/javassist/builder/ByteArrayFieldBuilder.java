package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ByteArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instance_var_name = context.instance_var_name;
        String lenRes=context.lenRes;
        switch (packetField.singleLen()) {
            case 1: {
                break;
            }
            default: {
                JavassistUtil.packetField_singleLen_notSupport(field);
            }
        }
        String arr_var_name = fieldVarName + "_arr";
        JavassistUtil.append(body, "byte[] {}=new byte[{}];\n", arr_var_name, lenRes);
        JavassistUtil.append(body, "{}.readBytes({});\n", byteBuf_var_name, arr_var_name);
        JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName, arr_var_name);
    }
}

package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ByteArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        String lenRes=context.lenRes;
        if (packetField.singleLen() != 1) {
            JavassistUtil.packetFieldSingleLenNotSupport(field);
        }
        String arr_var_name = varNameField + "_arr";
        JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", arr_var_name, lenRes);
        JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arr_var_name);
        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), arr_var_name);
    }
}

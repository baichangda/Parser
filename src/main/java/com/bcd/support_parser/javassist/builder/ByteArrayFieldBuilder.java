package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ByteArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        String lenRes=context.lenRes;
        if (packetField.singleLen() != 1) {
            JavassistUtil.packetFieldSingleLenNotSupport(field);
        }
        String arr_var_name = varNameField + "_arr";
        JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", arr_var_name, lenRes);
        JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arr_var_name);
        JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, arr_var_name);
    }
}

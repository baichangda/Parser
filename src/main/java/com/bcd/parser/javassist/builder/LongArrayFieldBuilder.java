package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class LongArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        String lenRes=context.lenRes;
        switch (packetField.singleLen()) {
            case 4 -> {
                lenRes = "(" + lenRes + ")/4";
            }
            case 8 -> {
                lenRes = "(" + lenRes + ")/8";
            }
            default -> {
                JavassistUtil.packetFieldSingleLenNotSupport(field);
            }
        }
        String arr_var_name = varNameField + "_arr";
        JavassistUtil.append(body,"final long[] {}=new long[{}];\n",arr_var_name, lenRes);
        JavassistUtil.append(body,"for(int i=0;i<{}.length;i++){\n",arr_var_name);
        switch (packetField.singleLen()) {
            case 4 -> {
                JavassistUtil.append(body, "{}[i]={}.readUnsignedInt();\n", arr_var_name, FieldBuilder.varNameByteBuf);
            }
            case 8 -> {
                JavassistUtil.append(body, "{}[i]={}.readLong();\n", arr_var_name, FieldBuilder.varNameByteBuf);
            }
        }
        body.append("}\n");

        JavassistUtil.append(body,"{}.{}({});\n", varNameInstance, setMethodName, arr_var_name);
    }
}

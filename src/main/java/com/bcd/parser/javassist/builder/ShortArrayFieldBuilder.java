package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ShortArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String instance_var_name = context.instance_var_name;
        String lenRes=context.lenRes;
        switch (packetField.singleLen()) {
            case 1: {
                break;
            }
            case 2: {
                lenRes = "(" + lenRes + ")/2";
                break;
            }
            default: {
                JavassistUtil.packetField_singleLen_notSupport(field);
            }
        }
        String arr_var_name = fieldVarName + "_arr";
        JavassistUtil.append(body,"short[] {}=new short[{}];\n",arr_var_name, lenRes);
        JavassistUtil.append(body,"for(int i=0;i<{}.length;i++){\n",arr_var_name);
        switch (packetField.singleLen()) {
            case 1: {
                JavassistUtil.append(body,"{}[i]={}.readUnsignedByte();\n",arr_var_name,byteBuf_var_name);
                break;
            }
            case 2: {
                JavassistUtil.append(body,"{}[i]={}.readShort();\n",arr_var_name,byteBuf_var_name);
                break;
            }
        }
        body.append("}\n");

        JavassistUtil.append(body,"{}.{}({});\n", instance_var_name, setMethodName, arr_var_name);
    }
}

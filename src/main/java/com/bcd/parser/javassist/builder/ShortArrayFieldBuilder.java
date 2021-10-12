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
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String varNameInstance = context.varNameInstance;
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
                JavassistUtil.packetFieldSingleLenNotSupport(field);
            }
        }
        String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body,"short[] {}=new short[{}];\n",arrVarName, lenRes);
        JavassistUtil.append(body,"for(int i=0;i<{}.length;i++){\n",arrVarName);
        switch (packetField.singleLen()) {
            case 1: {
                JavassistUtil.append(body,"{}[i]={}.readUnsignedByte();\n",arrVarName, FieldBuilder.varNameByteBuf);
                break;
            }
            case 2: {
                JavassistUtil.append(body,"{}[i]={}.readShort();\n",arrVarName, FieldBuilder.varNameByteBuf);
                break;
            }
        }
        body.append("}\n");

        JavassistUtil.append(body,"{}.{}({});\n", varNameInstance, setMethodName, arrVarName);
    }
}

package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ShortArrayFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        String lenRes=context.lenRes;
        switch (packetField.singleLen()) {
            case 1 : {
                break;
            }
            case 2 : {
                lenRes = "(" + lenRes + ")/2";
                break;
            }
            default : {
                JavassistUtil.packetFieldSingleLenNotSupport(field);
                break;
            }
        }
        String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body,"final short[] {}=new short[{}];\n",arrVarName, lenRes);
        JavassistUtil.append(body,"for(int i=0;i<{}.length;i++){\n",arrVarName);
        switch (packetField.singleLen()) {
            case 1 : {
                JavassistUtil.append(body, "{}[i]={}.readUnsignedByte();\n", arrVarName, FieldBuilder.varNameByteBuf);
                break;
            }
            case 2 : {
                JavassistUtil.append(body, "{}[i]={}.readShort();\n", arrVarName, FieldBuilder.varNameByteBuf);
                break;
            }
        }
        body.append("}\n");

        JavassistUtil.append(body,"{}.{}={};\n", varNameInstance, field.getName(), arrVarName);
    }
}

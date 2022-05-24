package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class StringFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        String lenRes;
        final int len = packetField.len();
        if (len == 0) {
            lenRes = JavassistUtil.replaceVarToFieldName(packetField.lenExpr(), context.varToFieldName, field);
        } else {
            lenRes = len + "";
        }
        String arrVarName=varNameField+"_arr";
        String discardLenVarName=varNameField+"_discardLen";
        JavassistUtil.append(body,"final byte[] {}=new byte[{}];\n",arrVarName,lenRes);
        JavassistUtil.append(body,"{}.readBytes({});\n", FieldBuilder.varNameByteBuf,arrVarName);
        JavassistUtil.append(body,"final int {}=0;\n",discardLenVarName);
        JavassistUtil.append(body,"for(int i={}.length-1;i>=0;i--){\n",arrVarName);
        JavassistUtil.append(body,"if({}[i]==0){\n",arrVarName);
        JavassistUtil.append(body,"{}++;\n",discardLenVarName);
        JavassistUtil.append(body,"}else{\n",discardLenVarName);
        JavassistUtil.append(body,"break;\n",discardLenVarName);
        JavassistUtil.append(body,"}\n",discardLenVarName);
        JavassistUtil.append(body,"}\n",discardLenVarName);
        JavassistUtil.append(body,"{}.{}=new String({},0,{}.length-{});\n",varNameInstance,field.getName(),arrVarName,arrVarName,discardLenVarName);
    }
}

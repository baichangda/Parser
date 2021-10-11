package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class StringFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String instanceVarName = context.instanceVarName;
        String lenRes;
        final int len = packetField.len();
        if (len == 0) {
            lenRes = JavassistUtil.replaceVarToFieldName(packetField.lenExpr(), context.varToFieldName, field);
        } else {
            lenRes = len + "";
        }
        String arr_var_name=fieldVarName+"_arr";
        String discardLen_var_name=fieldVarName+"_discardLen";
        JavassistUtil.append(body,"byte[] {}=new byte[{}];\n",arr_var_name,lenRes);
        JavassistUtil.append(body,"{}.readBytes({});\n",byteBuf_var_name,arr_var_name);
        JavassistUtil.append(body,"int {}=0;\n",discardLen_var_name);
        JavassistUtil.append(body,"for(int i={}.length-1;i>=0;i--){\n",arr_var_name);
        JavassistUtil.append(body,"if({}[i]==0){\n",arr_var_name);
        JavassistUtil.append(body,"{}++;\n",discardLen_var_name);
        JavassistUtil.append(body,"}else{\n",discardLen_var_name);
        JavassistUtil.append(body,"break;\n",discardLen_var_name);
        JavassistUtil.append(body,"}\n",discardLen_var_name);
        JavassistUtil.append(body,"}\n",discardLen_var_name);
        JavassistUtil.append(body,"{}.{}(new String({},0,{}.length-{}));\n",instanceVarName,setMethodName,arr_var_name,arr_var_name,discardLen_var_name);
    }
}

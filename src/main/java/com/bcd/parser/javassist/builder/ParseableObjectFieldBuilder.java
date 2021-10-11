package com.bcd.parser.javassist.builder;

import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;


public class ParseableObjectFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context){
        final StringBuilder body = context.body;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String instanceVarName = context.instanceVarName;
        final String fieldTypeClassName = context.field.getType().getName();
        if(parser.allInOne){
            parser.buildAppend(context);
        }else{
            String processContextVarName=fieldVarName+"_processContext";
            final String processContextClassName = FieldProcessContext.class.getName();
            JavassistUtil.append(body,"{} {}=new {}({},{},{});\n",processContextClassName,processContextVarName,processContextClassName,parser_var_name,instanceVarName,parentProcessContext_var_name);
            JavassistUtil.append(body,"{}.{}({}.parse({}.class,{},{}));\n",instanceVarName,setMethodName,parser_var_name,fieldTypeClassName,byteBuf_var_name,processContextVarName);
        }
    }
}

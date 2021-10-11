package com.bcd.parser.javassist.builder;

import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;


public class ParseableObjectFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context){
        final StringBuilder body = context.body;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String instance_var_name = context.instance_var_name;
        final String fieldTypeClassName = context.field.getType().getName();
        if(parser.allInOne){
            parser.buildAppend(context);
        }else{
            String processContextVarName=fieldVarName+"_processContext";
            final String processContext_class_name = FieldProcessContext.class.getName();
            JavassistUtil.append(body,"{} {}=new {}({},{},{});\n",processContext_class_name,processContextVarName,processContext_class_name,parser_var_name,instance_var_name,parentProcessContext_var_name);
            JavassistUtil.append(body,"{}.{}({}.parse({}.class,{},{}));\n",instance_var_name,setMethodName,parser_var_name,fieldTypeClassName,byteBuf_var_name,processContextVarName);
        }
    }
}

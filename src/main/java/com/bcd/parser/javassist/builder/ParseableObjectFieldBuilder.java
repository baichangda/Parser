package com.bcd.parser.javassist.builder;

import com.bcd.parser.javassist.util.JavassistUtil;


public class ParseableObjectFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context){
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = context.field.getType().getName();
        if(parser.allInOne){
            parser.buildAppend(context);
        }else{
            JavassistUtil.append(body,"{}.{}({}.parse({}.class,{},{}));\n",varNameInstance,setMethodName, FieldBuilder.varNameParser,fieldTypeClassName, FieldBuilder.varNameByteBuf,context.getClassProcessContextVarName());
        }
    }
}

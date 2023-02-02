package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.javassist.util.JavassistUtil;

public class FieldBuilder__F_bean extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = context.field.getType().getName();
        if(parser.allInOne){
            parser.buildAppend(context);
        }else{
            JavassistUtil.append(body,"{}.{}={}.parse({}.class,{},{});\n",varNameInstance,context.field.getName(), FieldBuilder.varNameParser,fieldTypeClassName, FieldBuilder.varNameByteBuf,context.getClassProcessContextVarName());
        }
    }
}

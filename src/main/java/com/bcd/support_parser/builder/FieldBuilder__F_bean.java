package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bean extends FieldBuilder{
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldTypeClassName = context.field.getType().getName();
        JavassistUtil.append(body,"{}.{}={}.parse({}.class,{},{});\n",varNameInstance,context.field.getName(), FieldBuilder.varNameParser,fieldTypeClassName, FieldBuilder.varNameByteBuf,context.getProcessContextVarName());
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        final String fieldTypeClassName = field.getType().getName();
        JavassistUtil.append(body,"{}.deParse({},{},{});\n",varNameInstance+"."+ fieldName, FieldBuilder.varNameParser,fieldTypeClassName, FieldBuilder.varNameByteBuf,context.getProcessContextVarName());
    }
}

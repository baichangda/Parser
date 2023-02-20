package com.bcd.support_parser.builder;

import com.bcd.support_parser.Parser;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bean extends FieldBuilder{
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String fieldTypeClassName = context.field.getType().getName();
        final String parserClassName = Parser.class.getName();
        JavassistUtil.append(body,"{}.{}={}.parse({}.class,{},{});\n", FieldBuilder.varNameInstance,context.field.getName(), parserClassName,fieldTypeClassName, FieldBuilder.varNameByteBuf,context.getProcessContextVarName());
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final String fieldTypeClassName = field.getType().getName();
        final String parserClassName = Parser.class.getName();
        JavassistUtil.append(body,"{}.deParse({},{},{});\n", FieldBuilder.varNameInstance +"."+ fieldName, parserClassName,fieldTypeClassName, FieldBuilder.varNameByteBuf,context.getProcessContextVarName());
    }
}

package com.bcd.support_parser.impl.someip.javassist.builder;

import com.bcd.support_parser.anno.F_userDefine;
import com.bcd.support_parser.javassist.builder.BuilderContext;
import com.bcd.support_parser.javassist.builder.FieldBuilder;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class OffsetBuilder extends FieldBuilder{

    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_userDefine f_userDefine = field.getAnnotation(F_userDefine.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        JavassistUtil.append(body,"{} {}={}.readInt()>>4;\n", fieldTypeClassName,varNameField, FieldBuilder.varNameByteBuf);
        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
        context.varToFieldName.put(f_userDefine.var(),varNameField);
    }
}

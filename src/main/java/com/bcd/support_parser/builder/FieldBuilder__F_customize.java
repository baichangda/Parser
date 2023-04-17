package com.bcd.support_parser.builder;


import com.bcd.support_parser.anno.F_customize;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_customize extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_customize anno = field.getAnnotation(F_customize.class);
        final Class<?> builderClass = anno.builderClass();
        final Class<?> processorClass = anno.processorClass();
        if (builderClass == void.class) {
            if (processorClass == void.class) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have builderClass or processorClass", field.getDeclaringClass().getName(), field.getName(), F_customize.class.getName());
            } else {
                final StringBuilder body = context.body;
                final String varNameField = JavassistUtil.getFieldVarName(context);
                final String processorClassVarName = JavassistUtil.getProcessorVarName(processorClass);
                final String varNameInstance = FieldBuilder.varNameInstance;
                final String fieldTypeClassName = field.getType().getName();
                final String unBoxing = JavassistUtil.unBoxing(JavassistUtil.format("{}.process({},{})", processorClassVarName, FieldBuilder.varNameByteBuf, context.getProcessContextVarName()), field.getType());
                if (anno.var() == '0') {
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), unBoxing);
                } else {
                    JavassistUtil.append(body, "final {} {}={};\n", fieldTypeClassName, varNameField, unBoxing);
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                    context.varToFieldName.put(anno.var(), varNameField);
                }
            }
        } else {
            BuilderContext.fieldBuilderCache.computeIfAbsent(builderClass, k -> {
                try {
                    return (FieldBuilder) builderClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw BaseRuntimeException.getException(e);
                }
            }).buildParse(context);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final F_customize anno = field.getAnnotation(F_customize.class);
        final Class<?> builderClass = anno.builderClass();
        final Class<?> processorClass = anno.processorClass();
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varInstanceName = FieldBuilder.varNameInstance;
        final String valCode;
        if (anno.var() == '0') {
            valCode = varInstanceName + "." + field.getName();
        } else {
            JavassistUtil.append(body, "final {} {}={};\n", field.getType().getName(), varNameField, varInstanceName + "." + field.getName());
            valCode = varNameField;
        }
        if (builderClass == void.class) {
            if (processorClass == void.class) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have builderClass or processorClass", field.getDeclaringClass().getName(), field.getName(), F_customize.class.getName());
            } else {
                final String processorClassVarName = JavassistUtil.getProcessorVarName(processorClass);
                JavassistUtil.append(body, "{}.deProcess({},{},{});\n", processorClassVarName, FieldBuilder.varNameByteBuf, context.getProcessContextVarName(), valCode);
            }
        } else {
            BuilderContext.fieldBuilderCache.computeIfAbsent(builderClass, k -> {
                try {
                    return (FieldBuilder) builderClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw BaseRuntimeException.getException(e);
                }
            }).buildDeParse(context);
        }
    }
}

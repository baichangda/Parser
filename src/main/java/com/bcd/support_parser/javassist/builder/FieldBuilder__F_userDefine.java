package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.F_userDefine;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_userDefine extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final Field field = context.field;
        final F_userDefine anno = field.getAnnotation(F_userDefine.class);
        final Class<?> builderClass = anno.builderClass();
        final Class<?> processorClass = anno.processorClass();
        if (builderClass == void.class) {
            if (processorClass == void.class) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have builderClass or processorClass", field.getDeclaringClass().getName(), field.getName(), F_userDefine.class.getName());
            } else {
                final StringBuilder body = context.body;
                final String varNameField = JavassistUtil.getFieldVarName(context);
                final String processorClassVarName = JavassistUtil.toFirstLowerCase(processorClass.getSimpleName());
                final String varNameInstance = context.varNameInstance;
                final String fieldTypeClassName = field.getType().getName();
                final String unBoxing = JavassistUtil.unBoxing(JavassistUtil.format("{}.process({},{})", processorClassVarName, FieldBuilder.varNameByteBuf, context.getClassProcessContextVarName()), field.getType());
                if (anno.var() == '0') {
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), unBoxing);
                } else {
                    JavassistUtil.append(body, "final {} {}={};\n", fieldTypeClassName, varNameField, unBoxing);
                    JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                    context.varToFieldName.put(anno.var(), varNameField);
                }
            }
        } else {
            context.fieldBuilderCache.computeIfAbsent(builderClass, k -> {
                try {
                    return (FieldBuilder) builderClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw BaseRuntimeException.getException(e);
                }
            }).build(context);
        }
    }
}

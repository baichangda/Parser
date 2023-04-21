package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_float_ieee754;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_ieee754 extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_float_ieee754> annoClass = F_float_ieee754.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldType;
        if (float.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "float";
        } else if (double.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "double";
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
            fieldType = "";
        }
        final F_float_ieee754 anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;



        final String funcName;
        switch (anno.type()) {
            case Float32 -> {
                funcName = bigEndian ? "readFloat" : "readFloatLE";
            }
            case Float64 -> {
                funcName = bigEndian ? "readDouble" : "readDoubleLE";
            }
            default -> {
                JavassistUtil.notSupport_type(field, annoClass);
                funcName = null;
            }
        }
        if (anno.valPrecision() == -1) {
            JavassistUtil.append(body, "{}.{}={}.{}();\n", varNameInstance, field.getName(), varNameByteBuf, funcName);
        } else {
            JavassistUtil.append(body, "{}.{}=({}){}.format((double)({}.{}()),{});\n", varNameInstance, field.getName(), fieldType, JavassistUtil.class.getName(), varNameByteBuf, funcName, anno.valPrecision());
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_float_ieee754> annoClass = F_float_ieee754.class;
        final Field field = context.field;
        final F_float_ieee754 anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String funcName;
        final String funcParamTypeName;
        switch (anno.type()) {
            case Float32 -> {
                funcName = bigEndian ? "writeFloat" : "writeFloatLE";
                funcParamTypeName = "float";
            }
            case Float64 -> {
                funcName = bigEndian ? "writeDouble" : "writeDoubleLE";
                funcParamTypeName = "double";
            }
            default -> {
                JavassistUtil.notSupport_type(field, annoClass);
                funcName = null;
                funcParamTypeName = null;
            }
        }
        JavassistUtil.append(body, "{}.{}(({})({}.{}));\n", varNameByteBuf, funcName, funcParamTypeName, varNameInstance, fieldName);
    }
}

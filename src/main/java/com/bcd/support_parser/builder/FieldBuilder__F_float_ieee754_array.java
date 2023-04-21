package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_float_ieee754_array;
import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_ieee754_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_float_ieee754_array> annoClass = F_float_ieee754_array.class;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final String fieldName = field.getName();
        final String arrayElementType;
        if (float[].class.isAssignableFrom(fieldType)) {
            arrayElementType = "float";
        } else if (double[].class.isAssignableFrom(fieldType)) {
            arrayElementType = "double";
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
            arrayElementType = "";
        }

        final F_float_ieee754_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.order);
        
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), fieldName, F_skip.class.getName());
            } else {
                lenRes = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }


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
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementType, arrVarName, arrayElementType, lenRes);
        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        if (anno.valPrecision() == -1) {
            JavassistUtil.append(body, "{}[i]=({})({}.{}());\n", arrVarName, arrayElementType, varNameByteBuf, funcName);
        } else {
            JavassistUtil.append(body, "{}[i]=({}){}.format((double)({}.{}()),{});\n", arrVarName, fieldType, JavassistUtil.class.getName(), varNameByteBuf, funcName, anno.valPrecision());
        }
        JavassistUtil.append(body, "};\n");
        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, fieldName, arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_float_ieee754_array> annoClass = F_float_ieee754_array.class;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_float_ieee754_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.order);
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameInstance = FieldBuilder.varNameInstance;

        final String arrayElementType;
        if (float[].class.isAssignableFrom(fieldType)) {
            arrayElementType = "float";
        } else if (double[].class.isAssignableFrom(fieldType)) {
            arrayElementType = "double";
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
            arrayElementType = "";
        }

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

        final String varNameField = JavassistUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body, "final {}[] {}={}.{};\n", arrayElementType, arrVarName, varNameInstance, fieldName);
        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        JavassistUtil.append(body, "{}.{}(({})({}[i]));\n", varNameByteBuf, funcName, funcParamTypeName, arrVarName);
        JavassistUtil.append(body, "}\n");
    }
}

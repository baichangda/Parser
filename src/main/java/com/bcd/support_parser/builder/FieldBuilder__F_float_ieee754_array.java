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


        String funcName = null;
        switch (anno.type()) {
            case Float32: {
                switch (anno.order()) {
                    case BigEndian: {
                        funcName = "readFloat";
                        break;
                    }
                    case SmallEndian: {
                        funcName = "readFloatLE";
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_order(field, annoClass);
                    }
                }
                break;
            }
            case Float64: {
                switch (anno.order()) {
                    case BigEndian: {
                        funcName = "readDouble";
                        break;
                    }
                    case SmallEndian: {
                        funcName = "readDoubleLE";
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_order(field, annoClass);
                    }
                }
                break;
            }
            default: {
                JavassistUtil.notSupport_type(field, annoClass);
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

        String funcName = null;
        String funcParamTypeName = null;
        switch (anno.type()) {
            case Float32: {
                switch (anno.order()) {
                    case BigEndian: {
                        funcName = "writeFloat";
                        break;
                    }
                    case SmallEndian: {
                        funcName = "writeFloatLE";
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_order(field, annoClass);
                    }
                }
                funcParamTypeName = "float";
                break;
            }
            case Float64: {
                switch (anno.order()) {
                    case BigEndian: {
                        funcName = "writeDouble";
                        break;
                    }
                    case SmallEndian: {
                        funcName = "writeDoubleLE";
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_order(field, annoClass);
                    }
                }
                funcParamTypeName = "double";
                break;
            }
            default: {
                JavassistUtil.notSupport_type(field, annoClass);
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

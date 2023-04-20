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
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;

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
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameInstance = FieldBuilder.varNameInstance;
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
        JavassistUtil.append(body, "{}.{}(({})({}.{}));\n", varNameByteBuf, funcName, funcParamTypeName, varNameInstance, fieldName);
    }
}

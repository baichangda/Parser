package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.anno.F_float_array;
import com.bcd.support_parser.anno.F_integer_array;
import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_integer_array extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final Field field = context.field;
        final F_integer_array anno = context.field.getAnnotation(F_integer_array.class);
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                lenRes = JavassistUtil.replaceVarToFieldName(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        final Class<?> fieldTypeClass = field.getType();
        final String arrayElementType;
        if (byte[].class.isAssignableFrom(fieldTypeClass)) {
            arrayElementType = "byte";
        } else if (short[].class.isAssignableFrom(fieldTypeClass)) {
            arrayElementType = "short";
        } else if (int[].class.isAssignableFrom(fieldTypeClass)) {
            arrayElementType = "int";
        } else if (long[].class.isAssignableFrom(fieldTypeClass)) {
            arrayElementType = "long";
        } else {
            JavassistUtil.notSupport_fieldType(field, F_integer_array.class);
            arrayElementType = "";
        }

        final String varNameInstance = context.varNameInstance;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final StringBuilder body = context.body;
        final int singleLen = anno.singleLen();
        //如果类型是byte[]、singleLen==1、则特殊优化处理
        if (arrayElementType.equals("byte") && singleLen == 1) {
            JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", arrVarName, lenRes);
            JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arrVarName);
            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), arrVarName);
        } else {

            final String arrLenRes;
            switch (singleLen) {
                case 1: {
                    arrLenRes = lenRes;
                    break;
                }
                case 2: {
                    arrLenRes = "(" + lenRes + ")/2";
                    break;
                }
                case 4: {
                    arrLenRes = "(" + lenRes + ")/4";
                    break;
                }
                case 8: {
                    arrLenRes = "(" + lenRes + ")/8";
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, F_integer_array.class);
                    arrLenRes = "";
                    break;
                }
            }

            JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementType, arrVarName, arrayElementType, arrLenRes);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            final String varNameArrayElement = varNameField + "_arrEle";
            switch (singleLen) {
                case 1: {
                    JavassistUtil.append(body, "{} {}=({}){}.readUnsignedByte();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 2: {
                    JavassistUtil.append(body, "{} {}=({}){}.readUnsignedShort();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 4: {
                    JavassistUtil.append(body,"{} {}=({}){}.readUnsignedInt();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 8: {
                    JavassistUtil.append(body,"{} {}=({}){}.readLong();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                    break;
                }
                default: {
                    JavassistUtil.notSupport_len(field, F_integer_array.class);
                }
            }
            JavassistUtil.append(body, "{}[i]={};\n", arrVarName, JavassistUtil.replaceVarToValExpr(anno.valExpr(), varNameArrayElement));
            body.append("}\n");

            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), arrVarName);
        }
    }
}

package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_float_array;
import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;
import com.bcd.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_float_array anno = context.field.getAnnotation(F_float_array.class);
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                lenRes = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        final Class<?> fieldTypeClass = field.getType();
        final String arrayElementType;
        if (float[].class.isAssignableFrom(fieldTypeClass)) {
            arrayElementType = "float";
        } else if (double[].class.isAssignableFrom(fieldTypeClass)) {
            arrayElementType = "double";
        } else {
            JavassistUtil.notSupport_fieldType(field, F_float_array.class);
            arrayElementType = "";
        }
        final StringBuilder body = context.body;

        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String arrLenRes;
        switch (anno.singleLen()) {
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
                JavassistUtil.notSupport_singleLen(field, F_float_array.class);
                arrLenRes = "";
                break;
            }
        }
        String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementType, arrVarName, arrayElementType, arrLenRes);
        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        final String varNameArrayElement = varNameField + "_arrEle";
        switch (anno.singleLen()) {
            case 1: {
                JavassistUtil.append(body, "{} {}=({}){}.readUnsignedByte();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                break;
            }
            case 2: {
                JavassistUtil.append(body, "{} {}=({}){}.readUnsignedShort();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                break;
            }
            case 4: {
                JavassistUtil.append(body, "{} {}=({}){}.readUnsignedInt();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                break;
            }
            case 8: {
                JavassistUtil.append(body, "{} {}=({}){}.readLong();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                break;
            }
        }
        if (anno.valPrecision() == -1) {
            JavassistUtil.append(body, "{}[i]={};\n", arrVarName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameArrayElement));
        } else {
            JavassistUtil.append(body, "{}[i]=({}){}.format((double){},{});\n", arrVarName, arrayElementType, JavassistUtil.class.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameArrayElement), anno.valPrecision());
        }
        body.append("}\n");

        JavassistUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final F_float_array anno = context.field.getAnnotation(F_float_array.class);
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String valCode = FieldBuilder.varNameInstance + "." + fieldName;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameFieldArr;
        if (float[].class.isAssignableFrom(fieldTypeClass)) {
            varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final float[] {}={};\n", varNameFieldArr, valCode);
        } else if (double[].class.isAssignableFrom(fieldTypeClass)) {
            varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final double[] {}={};\n", varNameFieldArr, valCode);
        } else {
            JavassistUtil.notSupport_fieldType(field, F_float_array.class);
            varNameFieldArr = "";
        }
        switch (singleLen) {
            case 1: {
                String varNameFieldRes = varNameField + "_res";
                JavassistUtil.append(body, "final byte[] {}=new byte[{}.length];\n", varNameFieldRes, varNameFieldArr);
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldRes);
                if (anno.valExpr().isEmpty()) {
                    JavassistUtil.append(body, "{}[i]=(byte){};\n", varNameFieldRes, varNameFieldArr + "[i]");
                } else {
                    JavassistUtil.append(body, "{}[i]=(byte)({});\n", varNameFieldRes, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                }
                JavassistUtil.append(body, "}\n");
                JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, varNameFieldRes);
                break;
            }
            case 2: {
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                if (anno.valExpr().isEmpty()) {
                    JavassistUtil.append(body, "{}.writeShort((short){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr + "[i]");
                } else {
                    JavassistUtil.append(body, "{}.writeShort((short)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                }
                JavassistUtil.append(body, "}\n");
                break;
            }
            case 4: {
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                if (anno.valExpr().isEmpty()) {
                    JavassistUtil.append(body, "{}.writeInt((int){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr + "[i]");
                } else {
                    JavassistUtil.append(body, "{}.writeInt((int)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                }
                JavassistUtil.append(body, "}\n");
                break;
            }
            case 8: {
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                if (anno.valExpr().isEmpty()) {
                    JavassistUtil.append(body, "{}.writeLong((long){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr + "[i]");
                } else {
                    JavassistUtil.append(body, "{}.writeLong((long)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                }
                JavassistUtil.append(body, "}\n");
                break;
            }
            default: {
                JavassistUtil.notSupport_singleLen(field, F_float_array.class);
            }
        }
    }
}

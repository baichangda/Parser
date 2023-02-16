package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.anno.F_integer_array;
import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.javassist.util.JavassistUtil;
import com.bcd.support_parser.javassist.util.RpnUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;

public class FieldBuilder__F_integer_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
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

        final String varNameInstance = FieldBuilder.varNameInstance;
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
                    JavassistUtil.append(body, "{} {}=({}){}.readUnsignedInt();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 8: {
                    JavassistUtil.append(body, "{} {}=({}){}.readLong();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf);
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

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final F_integer_array anno = context.field.getAnnotation(F_integer_array.class);
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final StringBuilder body = context.body;
        final String varNameInstance = context.varNameInstance;
        final String fieldName = field.getName();
        final String valCode = varNameInstance + "." + fieldName;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        if (byte[].class.isAssignableFrom(fieldTypeClass)) {
            switch (singleLen) {
                case 1: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, valCode);
                    } else {
                        String varNameFieldArr = varNameField + "_arr";
                        String varNameFieldRes = varNameField + "_res";
                        JavassistUtil.append(body, "final byte[] {}={};\n", varNameFieldArr, valCode);
                        JavassistUtil.append(body, "final byte[] {}=new byte[{}.length];\n", varNameFieldRes, varNameFieldArr);
                        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldRes);
                        JavassistUtil.append(body, "{}[i]={};\n", varNameFieldRes, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        JavassistUtil.append(body, "}\n");
                        JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, varNameFieldRes);
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, F_integer_array.class);
                }
            }
        } else if (short[].class.isAssignableFrom(fieldTypeClass)) {
            String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final short[] {}={};\n", varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            switch (singleLen) {
                case 1: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeByte((byte){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr+"[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeByte((byte)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                case 2: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeShort({});\n", FieldBuilder.varNameByteBuf, varNameFieldArr+"[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeShort((short)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, F_integer_array.class);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else if (int[].class.isAssignableFrom(fieldTypeClass)) {
            String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final int[] {}={};\n", varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            switch (singleLen) {
                case 2: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeShort((short){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr+"[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeShort((short)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                case 4: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeInt({});\n", FieldBuilder.varNameByteBuf, varNameFieldArr+"[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeInt((int)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, F_integer_array.class);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else if (long[].class.isAssignableFrom(fieldTypeClass)) {
            String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final long[] {}={};\n", varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            switch (singleLen) {
                case 4: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeInt((int){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr+"[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeInt((int)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                case 8: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeLong({});\n", FieldBuilder.varNameByteBuf, varNameFieldArr+"[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeLong((long)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToValExpr(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, F_integer_array.class);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else {
            JavassistUtil.notSupport_fieldType(field, F_integer_array.class);
        }
    }
}

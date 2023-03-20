package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_integer_array;
import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;
import com.bcd.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_integer_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_integer_array> annoClass = F_integer_array.class;
        final F_integer_array anno = context.field.getAnnotation(annoClass);
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
        final int singleLen = anno.singleLen();
        final String valExpr = anno.valExpr();
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        if (byte[].class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", arrVarName, lenRes);
            switch (singleLen) {
                case 1: {
                    if (valExpr.isEmpty()) {
                        JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arrVarName);
                    } else {
                        final String varNameArrayElement = varNameField + "_arrEle";
                        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
                        JavassistUtil.append(body, "byte {}={}.readByte();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                        JavassistUtil.append(body, "{}[i]=(byte)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
                        JavassistUtil.append(body, "}\n");
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
        } else if (short[].class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final short[] {}=new short[{}];\n", arrVarName, "(" + lenRes + ")/" + singleLen);
            final String varNameArrayElement = varNameField + "_arrEle";
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            switch (singleLen) {
                case 1: {
                    JavassistUtil.append(body, "short {}={}.readUnsignedByte();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 2: {
                    switch (anno.order()) {
                        case BigEndian: {
                            JavassistUtil.append(body, "short {}={}.readShort();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        case SmallEndian: {
                            JavassistUtil.append(body, "short {}={}.readShortLE();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "{}[i]=(short)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
            JavassistUtil.append(body, "}\n");
        } else if (int[].class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final int[] {}=new int[{}];\n", arrVarName, "(" + lenRes + ")/" + singleLen);
            final String varNameArrayElement = varNameField + "_arrEle";
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            switch (singleLen) {
                case 2: {
                    switch (anno.order()) {
                        case BigEndian: {
                            JavassistUtil.append(body, "int {}={}.readUnsignedShort();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        case SmallEndian: {
                            JavassistUtil.append(body, "int {}={}.readUnsignedShortLE();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    break;
                }
                case 4: {
                    switch (anno.order()) {
                        case BigEndian: {
                            JavassistUtil.append(body, "int {}={}.readInt();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        case SmallEndian: {
                            JavassistUtil.append(body, "int {}={}.readIntLE();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "{}[i]=(int)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
            JavassistUtil.append(body, "}\n");
        } else if (long[].class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final long[] {}=new long[{}];\n", arrVarName, "(" + lenRes + ")/" + singleLen);
            final String varNameArrayElement = varNameField + "_arrEle";
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            switch (singleLen) {
                case 4: {
                    switch (anno.order()) {
                        case BigEndian: {
                            JavassistUtil.append(body, "long {}={}.readUnsignedInt();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        case SmallEndian: {
                            JavassistUtil.append(body, "long {}={}.readUnsignedIntLE();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    break;
                }
                case 8: {
                    switch (anno.order()) {
                        case BigEndian: {
                            JavassistUtil.append(body, "long {}={}.readLong();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        case SmallEndian: {
                            JavassistUtil.append(body, "long {}={}.readLongLE();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }

                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "{}[i]=(long)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
            JavassistUtil.append(body, "}\n");

        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
        }
        JavassistUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_integer_array> annoClass = F_integer_array.class;
        final F_integer_array anno = context.field.getAnnotation(annoClass);
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
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
                        JavassistUtil.append(body, "{}[i]={};\n", varNameFieldRes, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        JavassistUtil.append(body, "}\n");
                        JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, varNameFieldRes);
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
        } else if (short[].class.isAssignableFrom(fieldTypeClass)) {
            String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final short[] {}={};\n", varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            switch (singleLen) {
                case 1: {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeByte((byte){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.writeByte((byte)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                case 2: {
                    String funcName = null;
                    switch (anno.order()) {
                        case BigEndian: {
                            funcName = "writeShort";
                            break;
                        }
                        case SmallEndian: {
                            funcName = "writeShortLE";
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((short)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else if (int[].class.isAssignableFrom(fieldTypeClass)) {
            String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final int[] {}={};\n", varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            switch (singleLen) {
                case 2: {
                    String funcName = null;
                    switch (anno.order()) {
                        case BigEndian: {
                            funcName = "writeShort";
                            break;
                        }
                        case SmallEndian: {
                            funcName = "writeShortLE";
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}((short){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((short)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                case 4: {
                    String funcName = null;
                    switch (anno.order()) {
                        case BigEndian: {
                            funcName = "writeInt";
                            break;
                        }
                        case SmallEndian: {
                            funcName = "writeIntLE";
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else if (long[].class.isAssignableFrom(fieldTypeClass)) {
            String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final long[] {}={};\n", varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            switch (singleLen) {
                case 4: {
                    String funcName = null;
                    switch (anno.order()) {
                        case BigEndian: {
                            funcName = "writeInt";
                            break;
                        }
                        case SmallEndian: {
                            funcName = "writeIntLE";
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}((int){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                case 8: {
                    String funcName = null;
                    switch (anno.order()) {
                        case BigEndian: {
                            funcName = "writeLong";
                            break;
                        }
                        case SmallEndian: {
                            funcName = "writeLongLE";
                            break;
                        }
                        default: {
                            JavassistUtil.notSupport_order(field, annoClass);
                        }
                    }
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((long)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    break;
                }
                default: {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
        }
    }
}

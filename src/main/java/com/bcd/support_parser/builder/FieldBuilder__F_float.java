package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_float;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;
import com.bcd.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class FieldBuilder__F_float extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_float> annoClass = F_float.class;
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
        final F_float anno = context.field.getAnnotation(annoClass);
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        if (anno.len() == 0) {
            if (anno.bit() == 0) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] must have len or bit", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
            } else {
                final int bit = anno.bit();
                if (bit < 1 || bit > 32) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] bit must in range [1,32]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
                }
                final Map<String, int[]> fieldNameToBitInfo = context.fieldNameToBitInfo;
                String varNameBitBytes = context.varNameBitBytes;
                final int[] ints = fieldNameToBitInfo.get(field.getName());
                if (varNameBitBytes == null) {
                    varNameBitBytes = varNameField + "_bitBytes";
                    JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", varNameBitBytes, ints[2]);
                    JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, varNameBitBytes);
                    context.varNameBitBytes = varNameBitBytes;
                }
                JavassistUtil.append(body, "final int {}={}.getBitVal({},{},{});\n", varNameField, JavassistUtil.class.getName(), varNameBitBytes, ints[0], bit);
                if (ints[1] == 1) {
                    context.varNameBitBytes = null;
                }
            }
        } else {
            switch (anno.len()) {
                case 1: {
                    JavassistUtil.append(body, "{} {}=({}){}.readUnsignedByte();", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 2: {
                    JavassistUtil.append(body, "{} {}=({}){}.readUnsignedShort();", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 4: {
                    JavassistUtil.append(body, "{} {}=({}){}.readUnsignedInt();", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 8: {
                    JavassistUtil.append(body, "{} {}=({}){}.readLong();", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf);
                    break;
                }
                default: {
                    JavassistUtil.notSupport_len(field, annoClass);
                    break;
                }
            }
        }
        if (anno.valPrecision() == -1) {
            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
        } else {
            JavassistUtil.append(body, "{}.{}=({}){}.format((double){},{});\n", varNameInstance, field.getName(), fieldType, JavassistUtil.class.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField), anno.valPrecision());
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_float> annoClass = F_float.class;
        final Field field = context.field;
        final F_float anno = context.field.getAnnotation(annoClass);
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String valCode;
        if (anno.valExpr().isEmpty()) {
            valCode = varNameInstance + "." + fieldName;
        } else {
            final String reverseExpr = RpnUtil.reverseExpr(anno.valExpr());
            valCode = JavassistUtil.replaceValExprToCode(reverseExpr, varNameInstance + "." + fieldName);
        }
        if (anno.len() == 0) {
            if (anno.bit() == 0) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] must have len or bit", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
            } else {
                final int bit = anno.bit();
                if (bit < 1 || bit > 32) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] bit must in range [1,32]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
                }
                final Map<String, int[]> fieldNameToBitInfo = context.fieldNameToBitInfo;
                String varNameBitBytes = context.varNameBitBytes;
                final int[] ints = fieldNameToBitInfo.get(field.getName());
                if (varNameBitBytes == null) {
                    varNameBitBytes = varNameField + "_bitBytes";
                    JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", varNameBitBytes, ints[2]);
                    context.varNameBitBytes = varNameBitBytes;
                }
                JavassistUtil.append(body, "{}.putBitVal((int)({}),{},{},{});\n", JavassistUtil.class.getName(), valCode, varNameBitBytes, ints[0], bit);
                if (ints[1] == 1) {
                    JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, varNameBitBytes);
                    context.varNameBitBytes = null;
                }
            }
        } else {
            switch (anno.len()) {
                case 1: {
                    JavassistUtil.append(body, "{}.writeByte((byte)({}));\n", FieldBuilder.varNameByteBuf, valCode);
                    break;
                }
                case 2: {
                    JavassistUtil.append(body, "{}.writeShort((short)({}));\n", FieldBuilder.varNameByteBuf, valCode);
                    break;
                }
                case 4: {
                    JavassistUtil.append(body, "{}.writeInt((int)({}));\n", FieldBuilder.varNameByteBuf, valCode);
                    break;
                }
                case 8: {
                    JavassistUtil.append(body, "{}.writeLong((long)({}));\n", FieldBuilder.varNameByteBuf, valCode);
                    break;
                }
                default: {
                    JavassistUtil.notSupport_len(field, annoClass);
                    break;
                }
            }
        }
    }
}

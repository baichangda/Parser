package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_integer;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;
import com.bcd.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class FieldBuilder__F_integer extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_integer> annoClass = F_integer.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldTypeName = fieldTypeClass.getName();
        final F_integer anno = context.field.getAnnotation(annoClass);
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
                if (fieldTypeClass.isEnum()) {
                    JavassistUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeClass.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
                } else {
                    JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
                }
                if (ints[1] == 1) {
                    context.varNameBitBytes = null;
                }
            }
        } else {
            if (byte.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 1: {
                        JavassistUtil.append(body, "final byte {}={}.readByte();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (short.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 1: {
                        JavassistUtil.append(body, "final short {}={}.readUnsignedByte();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    case 2: {
                        JavassistUtil.append(body, "final short {}={}.readShort();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (int.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 2: {
                        JavassistUtil.append(body, "final int {}={}.readUnsignedShort();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    case 4: {
                        JavassistUtil.append(body, "final int {}={}.readInt();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (long.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 4: {
                        JavassistUtil.append(body, "final long {}={}.readUnsignedInt();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    case 8: {
                        JavassistUtil.append(body, "final long {}={}.readLong();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (fieldTypeClass.isEnum()) {
                switch (anno.len()) {
                    case 1: {
                        JavassistUtil.append(body, "final short {}={}.readUnsignedByte();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    case 2: {
                        JavassistUtil.append(body, "final int {}={}.readUnsignedShort();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    case 4: {
                        JavassistUtil.append(body, "final int {}={}.readInt();\n", varNameField, FieldBuilder.varNameByteBuf);
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else {
                JavassistUtil.notSupport_fieldType(field, annoClass);
            }
        }


        final char var = anno.var();
        if (var != '0') {
            context.varToFieldName.put(var, varNameField);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_integer> annoClass = F_integer.class;
        final Field field = context.field;
        final F_integer anno = context.field.getAnnotation(annoClass);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        final char var = anno.var();
        String valCode;
        //先判断是否是枚举类型、如果是枚举转换为int
        if (fieldType.isEnum()) {
            valCode = JavassistUtil.format("{}.toInteger()", varNameInstance + "." + fieldName);
        } else {
            valCode = varNameInstance + "." + fieldName;
        }

        //判断是否用到变量中、如果用到了、需要定义变量
        if (var != '0') {
            JavassistUtil.append(body, "final {} {}={};\n", fieldType.getName(), varNameField, valCode);
            context.varToFieldName.put(var, varNameField);
        }

        //最后判断是否用了值表达式、如果用了、进行表达式处理
        if(!anno.valExpr().isEmpty()){
            valCode = JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), valCode);
        }

        if (anno.len() == 0) {
            if (anno.bit() == 0) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] must have len or bit", field.getDeclaringClass().getName(), fieldName, annoClass.getName());
            } else {
                final int bit = anno.bit();
                if (bit < 1 || bit > 32) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] bit must in range [1,32]", field.getDeclaringClass().getName(), fieldName, annoClass.getName());
                }
                final Map<String, int[]> fieldNameToBitInfo = context.fieldNameToBitInfo;
                String varNameBitBytes = context.varNameBitBytes;
                final int[] ints = fieldNameToBitInfo.get(fieldName);
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

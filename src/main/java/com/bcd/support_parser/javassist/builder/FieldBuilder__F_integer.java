package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.anno.F_integer;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class FieldBuilder__F_integer extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final Class<F_integer> annoClass = F_integer.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldType;
        if (byte.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "byte";
        } else if (short.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "short";
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "int";
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "long";
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
            fieldType = "";
        }
        final F_integer anno = context.field.getAnnotation(annoClass);
        final StringBuilder body = context.body;
        final String varNameInstance = context.varNameInstance;
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
                    JavassistUtil.append(body, "final {} {}={}.readUnsignedByte();\n", fieldType, varNameField, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 2: {
                    JavassistUtil.append(body, "final {} {}={}.readUnsignedShort();\n", fieldType, varNameField, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 4: {
                    JavassistUtil.append(body, "final {} {}={}.readUnsignedInt();\n", fieldType, varNameField, FieldBuilder.varNameByteBuf);
                    break;
                }
                case 8: {
                    JavassistUtil.append(body, "final {} {}={}.readLong();\n", fieldType, varNameField, FieldBuilder.varNameByteBuf);
                    break;
                }
                default: {
                    JavassistUtil.notSupport_len(field, annoClass);
                    break;
                }
            }
        }

        JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldType, JavassistUtil.replaceVarToValExpr(anno.valExpr(), varNameField));
        final char var = anno.var();
        if (var != '0') {
            context.varToFieldName.put(var, varNameField);
        }
    }
}

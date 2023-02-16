package com.bcd.support_parser.util;


import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.builder.BuilderContext;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class JavassistUtil {
    public static void notSupport_fieldType(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_len(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_singleLen(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static String toFirstLowerCase(final String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toFirstUpperCase(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getVarName(final BuilderContext context, final String suffix) {
        final StringBuilder sb = new StringBuilder();
        BuilderContext temp = context;
        while (temp != null) {
            sb.insert(0, toFirstLowerCase(temp.field.getDeclaringClass().getSimpleName()) + "_");
            temp = temp.parentContext;
        }
        sb.append(suffix);
        return sb.toString();
    }

    public static String getFieldVarName(final BuilderContext context) {
        return getVarName(context, context.field.getName());
    }

    public static String replaceVarToValExpr(final String expr, final String valExpr) {
        if (expr.isEmpty()) {
            return valExpr;
        }
        final StringBuilder sb = new StringBuilder();
        final char[] chars = expr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')' && c != '.' && !Character.isDigit(c)) {
                sb.append(valExpr);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String unBoxing(final String num, final Class clazz) {
        if (clazz == byte.class) {
            return num + ".byteValue()";
        } else if (clazz == short.class) {
            return num + ".shortValue()";
        } else if (clazz == int.class) {
            return num + ".intValue()";
        } else if (clazz == long.class) {
            return num + ".longValue()";
        } else if (clazz == float.class) {
            return num + ".floatValue()";
        } else if (clazz == double.class) {
            return num + ".doubleValue()";
        } else {
            return num;
        }
    }


    public static String replaceVarToFieldName(final String expr, final Map<Character, String> map, final Field field) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = expr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && !Character.isDigit(c)) {
                final String s = map.get(c);
                if (s == null) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] expr[{}] can't find char[{}] value", field.getDeclaringClass().getName(), field.getName(), expr, c);
                }
                sb.append(s);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将信息转换为格式化
     * 使用方式和sl4j log一样、例如
     * {@link org.slf4j.Logger#info(String, Object...)}
     *
     * @param message
     * @param params
     * @return
     */
    public static String format(final String message, final Object... params) {
        return MessageFormatter.arrayFormat(message, params, null).getMessage();
    }

    public static void append(final StringBuilder sb, final String message, Object... params) {
        sb.append(format(message, params));
    }

    public static int getBitVal(byte[] bytes, int bitOffset, int bitLen) {
        final int startByteIndex = bitOffset / 8;
        final int endBitOffset = bitOffset + bitLen - 1;
        final int endByteIndex = endBitOffset / 8;
        final int byteLen = endByteIndex - startByteIndex + 1;
//        System.out.println("startByteIndex["+startByteIndex+"] endByteIndex["+endByteIndex+"] byteLen["+byteLen+"] bitOffset["+bitOffset+"] endBitOffset["+endBitOffset+"]");
        int c = bytes[endByteIndex] & 0xff;
        for (int i = endByteIndex - 1; i >= startByteIndex; i--) {
            c |= ((bytes[i] & 0xff) << ((endByteIndex - i) * 8));
        }
//        printBinaryString(byteLen + "个字节转换为int的二进制表示", c, byteLen);
        final int right = byteLen * 8 - bitOffset - bitLen;
//        printBinaryString("右移" + right + "后的结果", c >>> right, byteLen);
//        printBinaryString("需要进行&运算", (0x01 << bitLen) - 1, byteLen);
        final int res = (c >>> right) & ((0x01 << bitLen) - 1);
//        printBinaryString("最后结果二进制表示", res, byteLen);
        return res;
    }

    public static void putBitVal(int val, byte[] bytes, int bitOffset, int bitLen) {
        final int startByteIndex = bitOffset / 8;
        final int endBitOffset = bitOffset + bitLen - 1;
        final int endByteIndex = endBitOffset / 8;
        final int byteLen = endByteIndex - startByteIndex + 1;
        final int left = byteLen * 8 - bitOffset - bitLen;
        final int newVal = val << left;
        for (int i = endByteIndex; i >= startByteIndex; i--) {
            int right = (endByteIndex - i) * 8;
            bytes[i] = (byte) (bytes[i] | ((newVal >> right) & 0xff));
        }
    }

    private static void printBinaryString(String prepend, int i, int byteLen) {
        System.out.println(String.format(prepend + "---%" + byteLen * 8 + "s", Integer.toBinaryString(i)).replaceAll(" ", "0"));
    }

    static final double[] pows;

    static {
        pows = new double[10];
        for (int i = 0; i < pows.length; i++) {
            pows[i] = Math.pow(10, i);
        }
    }

    public static double format(double d, int i) {
        if (d > 0) {
            if (i == 0) {
                return Math.floor(d);
            } else {
                return Math.floor(d * pows[i]) / pows[i];
            }

        } else if (d < 0) {
            if (i == 0) {
                return Math.ceil(d);
            } else {
                return Math.ceil(d * pows[i]) / pows[i];
            }
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
//        byte[] bytes = new byte[]{(byte) 0};
//        System.out.println(getBitVal(bytes, 0, 1));
//        System.out.println(getBitVal(bytes, 1, 7));
        final int bitVal = getBitVal(new byte[]{0x01, 0x02, 0x03}, 2, 13);
        System.out.println(bitVal);
        byte[] bytes = new byte[3];
        putBitVal(bitVal, bytes, 2, 13);
        System.out.println(Arrays.toString(bytes));

//        final double format = JavassistUtil.format(1.23232d, 4);
//        System.out.println(format);
    }
}

package com.bcd.support_parser.util;


import com.bcd.support_parser.Parser;
import com.bcd.support_parser.anno.ByteOrder;
import com.bcd.support_parser.builder.FieldBuilder;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.builder.BuilderContext;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class JavassistUtil {

    public static void notSupport_type(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] type not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_order(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] order not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_fieldType(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_len(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_singleLen(final Field field, Class annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] singleLen not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    private static String toFirstLowerCase(final String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String getProcessorVarName(final Class processorClass) {
        return "_" + toFirstLowerCase(processorClass.getSimpleName());
    }

    /**
     * @param order
     * @param clazz
     * @return
     */
    public static boolean bigEndian(ByteOrder order, Class clazz) {
        ByteOrder configOrder = null;
        final String className = clazz.getName();
        for (Parser.ByteOrderConfig config : Parser.byteOrderConfigs) {
            if (className.startsWith(config.classPrefix())) {
                configOrder = config.order();
            }
        }

        if (configOrder == null) {
            if (order == ByteOrder.Default) {
                return true;
            } else {
                return order == ByteOrder.BigEndian;
            }
        } else {
            if (order == ByteOrder.Default) {
                if (configOrder == ByteOrder.Default) {
                    return true;
                } else {
                    return configOrder == ByteOrder.BigEndian;
                }
            } else {
                return order == ByteOrder.BigEndian;
            }
        }
    }

    /**
     * 定义类变量、解析其中的变量名称
     *
     * @param context
     * @param valDefine
     * @param params
     * @return
     */
    public static String defineClassVar(final BuilderContext context, Class varClass, final String valDefine, Object... params) {
        return context.classVarDefineToVarName.computeIfAbsent(format(valDefine, params), k -> {
            final int size = context.classVarDefineToVarName.size();
            final String varName = "_" + size + "_" + varClass.getSimpleName();
            final CtClass ctClass = context.implCc;
            try {
                final CtField ctField = CtField.make("private final " + varClass.getName() + " " + varName + "=" + k + ";\n", ctClass);
                ctClass.addField(ctField);
            } catch (CannotCompileException e) {
                throw BaseRuntimeException.getException(e);
            }
            return varName;
        });
    }

    public static String getFieldByteBufReaderIndexVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_byteBuf_readerIndex";
    }

    public static String getFieldByteBufWriterIndexVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_byteBuf_writerIndex";
    }

    public static String getFieldLogBytesVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_bytes";
    }

    public static void prependLogCode_parse(final BuilderContext context) {
        final String varName = getFieldByteBufReaderIndexVarName(context);
        append(context.body, "final int {}={}.readerIndex();\n", varName, FieldBuilder.varNameByteBuf);
    }

    public static void appendLogCode_parse(final BuilderContext context) {
        final String fieldByteBufReaderIndexVarName = getFieldByteBufReaderIndexVarName(context);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context);
        append(context.body, "final byte[] {}=new byte[{}.readerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName);
        append(context.body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName, fieldLogBytesVarName);
        append(context.body, "{}.logCollector_parse.collect_field({}.class,\"{}\",{},{},\"{}\");\n",
                Parser.class.getName(),
                context.field.getDeclaringClass().getName(),
                context.field.getName(),
                fieldLogBytesVarName,
                boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()),
                context.implCc.getSimpleName());
    }

    public static void prependLogCode_deParse(final BuilderContext context) {
        final String varName = getFieldByteBufWriterIndexVarName(context);
        append(context.body, "final int {}={}.writerIndex();\n", varName, FieldBuilder.varNameByteBuf);
    }

    public static void appendLogCode_deParse(final BuilderContext context) {
        final String fieldByteBufWriterIndexVarName = getFieldByteBufWriterIndexVarName(context);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context);
        append(context.body, "final byte[] {}=new byte[{}.writerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName);
        append(context.body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName, fieldLogBytesVarName);
        append(context.body, "{}.logCollector_deParse.collect_field({}.class,\"{}\",{},{},\"{}\");\n",
                Parser.class.getName(),
                context.field.getDeclaringClass().getName(),
                context.field.getName(),
                boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()),
                fieldLogBytesVarName,
                context.implCc.getSimpleName());
    }

    public static String getFieldVarName(final BuilderContext context) {
        return context.field.getName();
    }

    public static String replaceValExprToCode(final String expr, final String valExpr) {
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

    public static String boxing(final String num, final Class clazz) {
        if (clazz == byte.class) {
            return "Byte.valueOf(" + num + ")";
        } else if (clazz == short.class) {
            return "Short.valueOf(" + num + ")";
        } else if (clazz == int.class) {
            return "Integer.valueOf(" + num + ")";
        } else if (clazz == long.class) {
            return "Long.valueOf(" + num + ")";
        } else if (clazz == float.class) {
            return "Float.valueOf(" + num + ")";
        } else if (clazz == double.class) {
            return "Double.valueOf(" + num + ")";
        } else {
            return num;
        }
    }


    public static String replaceLenExprToCode(final String expr, final Map<Character, String> map, final Field field) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = expr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && !Character.isDigit(c)) {
                final String s = map.get(c);
                if (s == null) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] expr[{}] can't find char[{}] value", field.getDeclaringClass().getName(), field.getName(), expr, c);
                }
                //所有的len字段必须转化为int运算
                sb.append("(int)(").append(s).append(")");
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

    public static String prepend_2_0(int num) {
        return prepend(num + "", 2, "0");
    }

    public static String prepend_2_0(String str) {
        return prepend(str, 2, "0");
    }

    public static String prepend(String str, int len, String s) {
        final int length = str.length();
        if (length == len) {
            return str;
        } else if (length < len) {
            return s.repeat(len - length) + str;
        } else {
            return s.substring(0, len);
        }
    }

    public static void main(String[] args) {
//        byte[] bytes = new byte[]{(byte) 0};
//        System.out.println(getBitVal(bytes, 0, 1));
//        System.out.println(getBitVal(bytes, 1, 7));
        final byte[] source = {0x00, (byte) 0xe4};
        final int bitVal1 = getBitVal(source, 0, 1);
        final int bitVal2 = getBitVal(source, 1, 15);
        System.out.println(bitVal1);
        System.out.println(bitVal2);
        byte[] dest = new byte[2];
        putBitVal(bitVal1, dest, 0, 1);
        putBitVal(bitVal2, dest, 1, 15);
        System.out.println(Arrays.toString(dest));

//        final double format = JavassistUtil.format(1.23232d, 4);
//        System.out.println(format);
    }
}

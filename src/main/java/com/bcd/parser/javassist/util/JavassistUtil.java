package com.bcd.parser.javassist.util;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.javassist.builder.BuilderContext;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JavassistUtil {
    public static void packetFieldLenNotSupport(final Field field) {
        throw BaseRuntimeException.getException("class[{}] field[{}] @PacketField[len={}] not support", field.getDeclaringClass().getName(), field.getName(), field.getAnnotation(PacketField.class).len());
    }

    public static void packetFieldSingleLenNotSupport(final Field field) {
        throw BaseRuntimeException.getException("class[{}] field[{}] @PacketField[singleLen={}] not support", field.getDeclaringClass().getName(), field.getName(), field.getAnnotation(PacketField.class).singleLen());
    }

    public static String toFirstLowerCase(final String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toFirstUpperCase(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getVarName(final BuilderContext context,final String suffix) {
        final StringBuilder sb = new StringBuilder();
        BuilderContext temp = context;
        while (temp != null) {
            sb.insert(0,toFirstLowerCase(temp.field.getDeclaringClass().getSimpleName())+"_");
            temp = temp.parentContext;
        }
        sb.append(suffix);
        return sb.toString();
    }

    public static String getFieldVarName(final BuilderContext context) {
        return getVarName(context,context.field.getName());
    }

    public static String getSetMethodName(final Field field) {
        final String fieldName = field.getName();
        final String setMethodName = "set" + toFirstUpperCase(fieldName);
        return setMethodName;
    }

    public static String replaceVarToValExpr(final String expr, final String valExpr) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = expr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && !Character.isDigit(c)) {
                sb.append(valExpr);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String unBoxing(final String num,final Class clazz){
        if(clazz==byte.class){
            return num+".byteValue()";
        }else if(clazz==short.class){
            return num+".shortValue()";
        }else if(clazz==int.class){
            return num+".intValue()";
        }else if(clazz==long.class){
            return num+".longValue()";
        }else if(clazz==float.class){
            return num+".floatValue()";
        }else if(clazz==double.class){
            return num+".doubleValue()";
        }else{
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
}

package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * float[]、double[]
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_float_ieee754_array {

    /**
     * 浮点数类型
     * {@link FloatType_ieee754#Float}占用4字节
     * {@link FloatType_ieee754#Double}占用8字节
     */
    FloatType_ieee754 type();


    /**
     * 数组元素个数
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 数组元素个数表达式,配合var参数使用
     * 与{@link #len()}互斥
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     */
    String lenExpr() default "";

    /**
     * 表达式运算结果的精度、主要针对于float、double字段
     * -1代表不进行格式化精度
     */
    int valPrecision() default -1;

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.BigEndian;
}

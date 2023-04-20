package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * float、double
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_float_ieee754 {

    /**
     * 浮点数类型
     * {@link FloatType_ieee754#Float32}占用4字节
     * {@link FloatType_ieee754#Float64}占用8字节
     */
    FloatType_ieee754 type();

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

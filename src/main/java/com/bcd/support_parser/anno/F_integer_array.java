package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte[]、short[]、int[]、long[]
 * 数组长度=总字节数/singleLen
 * {@link #len()}和{@link #lenExpr()} 二选一、代表字段所占用总字节数
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_integer_array {
    /**
     * 占用字节数
     * 与{@link #lenExpr()}互斥
     */
    int len() default 0;

    /**
     * 字段所占字节长度表达式
     * 用于固定长度字段解析,配合var参数使用,代表的是Byte的长度
     * 与{@link #len()}互斥
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     */
    String lenExpr() default "";

    /**
     * 单个元素字节长度(用于字节数组转换成byte[]、short[]、int[]、long[]数组中单个元素对应字节数)
     * 不同的数据类型对应不同的长度
     * byte[]: 1
     * short[]: 1、2
     * int[]: 2、4
     * long[]: 4、8
     * 例如:
     * 原始为 byte[8] 字段数据 转换成 int[],
     * singleLen=2、调用readUnsignedShort、将得到int[4]
     * singleLen=4、调用readInt、将得到int[2]
     */
    int singleLen();

    /**
     * 值处理表达式、针对于数组中每个值
     * 在解析出的原始值得基础上,进行运算
     * 公式中的x变量代表字段原始的值
     * 注意:
     * 表达式需要符合java运算表达式规则
     * 例如:
     * x-10
     * x*10
     * (x+10)*100
     * (x+100)/100
     */
    String valExpr() default "";

    /**
     * 是则大端模式、否则小端模式
     */
    boolean bigEndian() default true;

}

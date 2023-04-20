package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * float[]、double[]
 * <p>
 * 数组长度=总字节数/singleLen
 * <p>
 * {@link #len()}和{@link #lenExpr()} 二选一、代表字段所占用总字节数
 *
 * 通过如下方式解析float
 * 首先得到整型数字、然后通过{@link #valExpr()}得到浮点数
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_float_integer_array {
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
     * 单个元素字节长度(用于字节数组转换成float[]、double[]数组中单个元素对应字节数)
     * 支持1、2、4、8
     * 例如:
     * 原始为 byte[8] 字段数据 转换成 double[],
     * singleLen=1、调用readUnsignedByte、然后将short转换为double、将得到double[8]
     * singleLen=2、调用readUnsignedInt、然后将int转换为double、将得到double[4]
     * singleLen=4、调用readUnsignedLong、然后将long转换为double 将得到double[2]
     * singleLen=8、调用readLong、然后将long转换为double 将得到double[1]
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
     * 表达式运算结果的精度、针对于数组中每个值
     * -1、代表不进行格式化精度
     */
    int valPrecision() default -1;

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.BigEndian;

    /**
     * 对原始值的解析视为无符号数字解析
     * 在获取原始值表现不同
     * 如果为true
     * 1字节 byteBuf.readByte()
     * 2字节 byteBuf.readShort()
     * 4字节 byteBuf.readInt()
     * 8字节 byteBuf.readLong()
     * 如果为false
     * 1字节 byteBuf.readUnsignedByte()
     * 2字节 byteBuf.readUnsignedShort()
     * 4字节 byteBuf.readUnsignedInt()
     * 8字节 byteBuf.readLong()
     */
    boolean unsigned() default true;
}

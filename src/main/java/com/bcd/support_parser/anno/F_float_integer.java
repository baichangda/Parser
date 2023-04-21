package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * float、double
 * 且值以多少个字节表示
 *
 * 通过如下方式解析float
 * 首先得到整型数字、然后通过{@link #valExpr()}得到浮点数
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_float_integer {
    /**
     * 占用字节数
     * 和{@link #bit()}互斥
     * 1,2,4,8
     */
    int len() default 0;

    /**
     * 占用bit位
     * 和{@link #len()}互斥
     * 1-32
     *
     * 注意:当此属性生效时候、{@link #bigEndian()}无效
     */
    int bit() default 0;

    /**
     * 值处理表达式
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
     * 表达式运算结果的精度、主要针对于float、double字段
     * -1代表不进行格式化精度
     */
    int valPrecision() default -1;

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.Default;

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

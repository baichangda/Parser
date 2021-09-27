package com.bcd.parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析字段注解,被标注的字段将会参与解析,并将解析后的值设置到字段中
 * 支持的字段类型如下:
 * <p>
 * byte/Byte
 * short/Short
 * int/Integer
 * long/Long
 * byte[]
 * short[]
 * int[]
 * long[]
 * float[]
 * double[]
 * String
 * Date
 * ByteBuf
 * List<{@link Parsable}>
 * Array[{@link Parsable}]
 * {@link Parsable}注解标注的自定义类型
 * <p>
 * 如果以上类型不满足解析需求,可以自行设置{@link #processorClass()}属性定义自定义解析器
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketField {
    /**
     * 序号
     * 用于标注当前注解字段在协议文档中的顺序
     * 例如: 1,2,3
     */
    int index();

    /**
     * 字段所占字节长度
     * 常量数值
     * 例如: 1,2,4,8
     */
    int len() default 0;

    /**
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供以下注解使用
     * {@link #lenExpr()}
     * {@link #listLenExpr()}
     * 例如: m,n,a
     */
    char var() default '0';

    /**
     * 字段所占字节长度表达式
     * 用于固定长度字段解析,配合var参数使用,代表的是Byte的长度
     * 例如:
     * m
     * m*n
     * a*b-1
     * a*(b-2)
     */
    String lenExpr() default "";

    /**
     * 是否跳过当前字段的解析
     * 必须满足如下条件才能跳过
     * 1、{@link #var()}必须未设置
     * 2、{@link #len()} 或者 {@link #lenExpr()} 必须设置了一个
     * <p>
     * 原理为解析到当前字段时候会得到当前字段所占字节长度、然后{@link io.netty.buffer.ByteBuf#skipBytes(int)}跳过
     * <p>
     */
    boolean skipParse() default false;

    /**
     * {@link Parsable}对象集合/数组长度表达式
     * 用于对象集合字段不定长度的解析,配合var参数使用,代表的是当前集合元素的个数
     * 适用于 List<TestBean> 字段类型
     * 例如:
     * m
     * m*n
     */
    String listLenExpr() default "";

    /**
     * 单个元素字节长度(用于字节数组转换成byte[]、short[]、int[]、long[]、float[]、double[]数组中单个元素对应字节数)
     * 例如:
     * 原始为 byte[8] 字段数据 转换成 int[],
     * 如果配比为 2: 则表示2个byte转换成一个int存入数组,因为一个int可以代表4个字节,所以int的高两位字节补全部补0,最后转换的长度为 int[4]
     * 如果配比为 4: 则表示4个byte转换成一个int存入数组,最后转换的长度为 int[2]
     */
    int singleLen() default 1;

    /**
     * 值处理表达式 y=x/a+b
     * 在解析出的原始值得基础上,进行偏移量运算,只对数字类型值有效
     * 公式中的x变量都代表字段原始的值
     * <p>
     * 注意:
     * 表达式顺序可以调整、可以加上括号
     */
    String valExpr() default "";

    /**
     * 处理类
     * 用于处理特殊情况,class类型必须是{@link com.bcd.parser.processer.FieldProcessor}子类
     */
    Class processorClass() default Void.class;
}

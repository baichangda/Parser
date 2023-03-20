package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte、short、int、long、枚举类
 * <p>
 * 枚举类
 * 仅支持当{@link #len()}为1、2、4时候、因为默认类型为int、8会产生精度丢失
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 * public static Example fromInteger(int i){}
 * public int toInteger(){}
 * }
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_integer {
    /**
     * 占用字节数
     * 和{@link #bit()}互斥
     * 不同的类型对应不同的长度
     * byte: 1
     * short: 1、2
     * int: 2、4
     * long: 4、8
     */
    int len() default 0;

    /**
     * 占用bit位
     * 和{@link #len()}互斥
     * 1-32
     *
     * 注意:当此属性生效时候、{@link #order()}无效
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
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.BigEndian;
}

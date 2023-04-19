package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 跳过此字段解析
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_skip {
    /**
     * 跳过字节模式
     * {@link SkipMode#Skip} 直接跳过当前指定长度的字节数
     * {@link SkipMode#Reserved} 从当前字段所属对象开始、计算到当前字段的字节数、跳过(规定长度-已经计入的字节数)
     */
    SkipMode mode() default SkipMode.Skip;

    /**
     * 占用字节数
     * 1-8
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
}

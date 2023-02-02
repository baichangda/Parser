package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于实体类集合字段
 *
 * {@link #listLen()}和{@link #listLenExpr()} 二选一、代表字段所占用总字节数
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bean_list {

    /**
     * 对象集合
     * 与{@link #listLenExpr()}互斥
     */
    int listLen() default 0;

    /**
     * 对象集合长度表达式
     * 用于对象集合字段不定长度的解析,配合var参数使用,代表的是当前集合元素的个数
     * 适用于 List<TestBean> 字段类型
     * 与{@link #listLen()}互斥
     * 例如:
     * m
     * m*n
     */
    String listLenExpr() default "";
}

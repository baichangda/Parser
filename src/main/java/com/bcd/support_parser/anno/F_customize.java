package com.bcd.support_parser.anno;

import com.bcd.support_parser.processor.Processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于任何字段
 * 用户自己实现解析逻辑
 * <p>
 * {@link #builderClass()}和{@link #processorClass()} 二选一
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface F_customize {
    /**
     * 处理类
     * 与{@link #builderClass()}互斥
     * 必须是{@link Processor}子类
     */
    Class<?> processorClass() default void.class;

    /**
     * asm构建类
     * 与{@link #processorClass()}互斥
     * 必须是{@link com.bcd.support_parser.builder.FieldBuilder}子类
     */
    Class<?> builderClass() default void.class;

    /**
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';
}

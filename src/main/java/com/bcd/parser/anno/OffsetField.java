package com.bcd.parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 偏移量字段计算
 * 用于处理协议中的偏移量
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OffsetField {
    /**
     * 数据源字段
     */
    String sourceField();

    /**
     * 计算公式
     * 公式中的任意变量都代表{@link #sourceField()}字段的值
     */
    String expr();

}

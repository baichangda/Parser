package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期字段、固定占用6字节、适用于如下类型
 * {@link java.util.Date}
 * int 此时代表时间戳秒
 * long 此时代表时间戳毫秒
 * {@link String} 此时会格式化成 yyyyMMddHHmmss格式、且时区为+8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_date {
    /**
     * 年份偏移量、结果年份=baseYear+原始值
     */
    int baseYear() default 2000;

    /**
     * 所代表的时区
     * 注意为什么用时区、因为如果使用偏移量、可能会存在夏令时问题
     */
    String zoneId() default "Asia/Shanghai";

    /**
     * 当字段为string时候
     * 此属性生效且代表日期格式
     */
    String formatWhenString() default "yyyy-MM-dd HH:mm:ss.SSS Z";
}

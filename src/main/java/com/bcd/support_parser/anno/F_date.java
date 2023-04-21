package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于如下类型
 * {@link java.util.Date}
 * int 此时代表时间戳秒
 * long 此时代表时间戳毫秒
 * {@link String} 此时使用{@link #stringFormat()}、{@link #stringZoneId()}格式化
 *
 * {@link DateMode#Bytes_yyMMddHHmmss} 协议定义6字节、分别代表 年月日时分秒 、年需要加上{@link #baseYear()}、需要结合时区{@link #zoneId()}完成解析
 * {@link DateMode#Bytes_yyyyMMddHHmmss} 协议定义7字节、分别代表 年月日时分秒、年占用2字节、需要结合时区{@link #zoneId()}完成解析
 * {@link DateMode#Uint64_millisecond} 协议定义uint64、代表毫秒
 * {@link DateMode#Uint64_second} 协议定义uint64、代表秒、转换为毫秒要*1000
 * {@link DateMode#Uint32_second} 协议定义uint32、代表秒、转换为毫秒要*1000
 * {@link DateMode#Float64_millisecond} 协议定义float64、代表毫秒
 * {@link DateMode#Float64_second} 协议定义float64、代表秒、精度为0.001、转换为毫秒要*1000
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_date {
    DateMode mode();

    /**
     * 如下模式时候
     * {@link DateMode#Bytes_yyMMddHHmmss}
     * {@link DateMode#Bytes_yyyyMMddHHmmss}
     * 用于表示原始值的时区
     * 注意时区可以为offset、例如+8、但是此时需要考虑夏令时问题
     */
    String zoneId() default "Asia/Shanghai";

    /**
     * 如下模式时候
     * {@link DateMode#Bytes_yyMMddHHmmss}
     * 年份偏移量、结果年份=baseYear+原始值
     */
    int baseYear() default 2000;

    /**
     * 如下模式时候
     * {@link DateMode#Bytes_yyyyMMddHHmmss} 此时只针对年才有大小端问题
     * {@link DateMode#Uint64_millisecond}
     * {@link DateMode#Uint64_second}
     * {@link DateMode#Uint32_second}
     * {@link DateMode#Float64_millisecond}
     * {@link DateMode#Float64_second}
     * 是否大端模式、否则小端模式
     */
    boolean bigEndian() default true;

    /**
     * 当字段为string类型时候
     * 需要指定翻译成string的时区、格式
     * 注意时区可以为offset、例如+8、但是此时需要考虑夏令时问题
     */
    String stringFormat() default "yyyyMMddHHmmss";
    String stringZoneId() default "Asia/Shanghai";

}

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
 *
 * 解析过程、分为两个步骤
 * 1、首先所有的输入数据源都会转换为时间戳毫秒(long类型)
 * 2、然后将时间戳转换为字段值
 *
 * 以下是步骤1不同{@link #mode()}的过程
 * {@link DateMode#Bytes_yyMMddHHmmss} 协议定义6字节、分别代表 年月日时分秒
 * 1、首先读取源数据获取 年月日时分秒、对 年加上{@link #baseYear()}
 * 2、根据{@link #zoneId()}转换为{@link java.time.ZonedDateTime}
 * 3、{@link java.time.ZonedDateTime}转换为时间戳毫秒
 *
 * {@link DateMode#Bytes_yyyyMMddHHmmss} 协议定义7字节、分别代表 年月日时分秒、年占用2字节
 * 1、首先读取源数据获取 年月日时分秒、读取年时候会使用{@link #order()}
 * 2、根据{@link #zoneId()}转换为{@link java.time.ZonedDateTime}
 * 3、{@link java.time.ZonedDateTime}转换为时间戳毫秒
 *
 * {@link DateMode#Uint64_millisecond} 协议定义uint64、代表时间戳毫秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳毫秒(long类型)
 *
 * {@link DateMode#Uint64_second} 协议定义uint64、代表时间戳秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳秒(long类型)
 * 2、时间戳秒*1000得到时间戳毫秒
 *
 * {@link DateMode#Uint32_second} 协议定义uint32、代表时间戳秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳秒(long类型)
 * 2、时间戳秒*1000得到时间戳毫秒
 *
 * {@link DateMode#Float64_millisecond} 协议定义float64、代表时间戳毫秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳毫秒(double类型)、转换数据类型为long类型
 *
 * {@link DateMode#Float64_second} 协议定义float64、代表秒、精度为0.001
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳秒(double类型)
 * 2、时间戳秒*1000得到时间戳毫秒(double类型)、转换数据类型为long类型
 *
 * 步骤2过程
 * 根据步骤1得到的时间戳毫秒、针对不同数据类型进行转换
 * {@link java.util.Date} new Date(long)
 * long 无需转换
 * int (int)(long/1000)
 * {@link String} 先转换为{@link java.time.ZonedDateTime}、然后使用{@link #stringFormat()}、{@link #stringZoneId()}格式化成字符串
 *
 *
 * 反解析步骤与解析过程相反
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_date {
    /**
     * {@link DateMode#Bytes_yyMMddHHmmss} 协议定义6字节、分别代表 年月日时分秒
     * {@link DateMode#Bytes_yyyyMMddHHmmss} 协议定义7字节、分别代表 年月日时分秒、年占用2字节
     * {@link DateMode#Uint64_millisecond} 协议定义uint64、代表时间戳毫秒
     * {@link DateMode#Uint64_second} 协议定义uint64、代表时间戳秒
     * {@link DateMode#Uint32_second} 协议定义uint32、代表时间戳秒
     * {@link DateMode#Float64_millisecond} 协议定义float64、代表时间戳毫秒
     * {@link DateMode#Float64_second} 协议定义float64、代表秒、精度为0.001
     */
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
     * 字节序模式
     * 如下模式时候
     * {@link DateMode#Bytes_yyyyMMddHHmmss} 此时只针对年才有大小端问题
     * {@link DateMode#Uint64_millisecond}
     * {@link DateMode#Uint64_second}
     * {@link DateMode#Uint32_second}
     * {@link DateMode#Float64_millisecond}
     * {@link DateMode#Float64_second}
     * 是否大端模式、否则小端模式
     */
    ByteOrder order() default ByteOrder.Default;

    /**
     * 当字段为string类型时候
     * 需要指定翻译成string的时区、格式
     * 注意时区可以为offset、例如+8、但是此时需要考虑夏令时问题
     */
    String stringFormat() default "yyyyMMddHHmmss";
    String stringZoneId() default "Asia/Shanghai";

}

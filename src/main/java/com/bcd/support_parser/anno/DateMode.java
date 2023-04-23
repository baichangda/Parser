package com.bcd.support_parser.anno;

/**
 * 说明参考{@link F_date#mode()}注释
 */
public enum DateMode {
    /**
     * 协议定义6字节、分别代表 年月日时分秒
     */
    Bytes_yyMMddHHmmss,
    /**
     * 协议定义7字节、分别代表 年月日时分秒、年占用2字节
     */
    Bytes_yyyyMMddHHmmss,
    /**
     * 协议定义uint64、代表时间戳毫秒
     */
    Uint64_millisecond,
    /**
     * 协议定义uint64、代表时间戳秒
     */
    Uint64_second,
    /**
     * 协议定义uint32、代表时间戳秒
     */
    Uint32_second,
    /**
     * 协议定义float64、代表时间戳毫秒
     */
    Float64_millisecond,
    /**
     * 协议定义float64、代表秒、精度为0.001、小数位代表毫秒
     */
    Float64_second,
}

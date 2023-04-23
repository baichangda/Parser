package com.bcd.support_parser.anno;

/**
 * 说明参考{@link F_date#mode()}注释
 */
public enum DateMode {
    Bytes_yyMMddHHmmss,
    Bytes_yyyyMMddHHmmss,
    Uint64_millisecond,
    Uint64_second,
    Uint32_second,
    Float64_millisecond,
    Float64_second,
}

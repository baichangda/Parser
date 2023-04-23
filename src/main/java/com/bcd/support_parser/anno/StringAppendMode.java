package com.bcd.support_parser.anno;

/**
 * 说明参考{@link F_string#appendMode()}注释
 */
public enum StringAppendMode {
    /**
     * 不补0
     */
    NoAppend,
    /**
     * 低内存地址补0
     */
    LowAddressAppend,
    /**
     * 高内存地址补0
     */
    HighAddressAppend
}

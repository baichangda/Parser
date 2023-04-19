package com.bcd.support_parser.anno;

public enum SkipMode {
    /**
     * 跳过当前字段字节数
     */
    Skip,
    /**
     * 从当前对象解析开始到当前字段应该预留的字节数、有剩余则跳过
     */
    Reserved
}

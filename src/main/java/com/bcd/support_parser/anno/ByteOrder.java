package com.bcd.support_parser.anno;

public enum ByteOrder {
    /**
     * 默认是大端模式、此属性是为了配合{@link T_order}完成优先级覆盖
     */
    Default,
    BigEndian,
    SmallEndian
}

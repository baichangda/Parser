module Parser.main {
    exports com.bcd.support_parser;
    exports com.bcd.support_parser.anno;
    exports com.bcd.support_parser.exception;
    exports com.bcd.support_parser.info;
    exports com.bcd.support_parser.processer;
    exports com.bcd.support_parser.processer.impl;
    exports com.bcd.support_parser.util;

    //实现
    exports com.bcd.support_parser.impl.gb32960.data;
    exports com.bcd.support_parser.impl.gb32960.processor;
    exports com.bcd.support_parser.impl.someip.data;
    exports com.bcd.support_parser.impl.someip.processor;

    requires io.netty.buffer;
    requires io.netty.common;

    requires jdk.unsupported;

    requires org.slf4j;
    requires org.apache.logging.log4j;
}
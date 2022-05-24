package com.bcd.support_parser.javassist.parser;

import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import io.netty.buffer.ByteBuf;

public interface JavassistParser<T> {
    T parse(final ByteBuf data, final FieldProcessContext context);
}

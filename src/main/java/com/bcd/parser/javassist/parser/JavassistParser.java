package com.bcd.parser.javassist.parser;

import com.bcd.parser.javassist.processor.FieldProcessContext;
import io.netty.buffer.ByteBuf;

public interface JavassistParser<T> {
    T parse(final ByteBuf data, final FieldProcessContext context);
}

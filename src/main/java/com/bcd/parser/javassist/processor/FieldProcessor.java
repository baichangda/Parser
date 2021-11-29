package com.bcd.parser.javassist.processor;

import com.bcd.parser.javassist.Parser;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FieldProcessor<T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public Parser parser;

    public abstract T process(final ByteBuf data, final FieldProcessContext context);
}

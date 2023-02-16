package com.bcd.support_parser.processor;

import com.bcd.support_parser.Parser;
import io.netty.buffer.ByteBuf;

public abstract class Processor<T> {

    public Parser parser;

    public abstract T process(final ByteBuf data, final ProcessContext parentContext);

    public void deProcess(final ByteBuf data, final ProcessContext parentContext,T instance){

    }
}

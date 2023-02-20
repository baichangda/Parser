package com.bcd.support_parser.processor;

import io.netty.buffer.ByteBuf;

public interface Processor<T> {

    T process(final ByteBuf data, final ProcessContext parentContext);

    default void deProcess(final ByteBuf data, final ProcessContext parentContext,T instance){

    }
}

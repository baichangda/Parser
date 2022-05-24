package com.bcd.support_parser.javassist.processor;

import com.bcd.support_parser.javassist.Parser;

public class FieldProcessContext {
    public final Object instance;
    public final Parser parser;
    public final FieldProcessContext parentContext;

    public FieldProcessContext(Parser parser, Object instance, FieldProcessContext parentContext) {
        this.parser = parser;
        this.instance = instance;
        this.parentContext = parentContext;
    }
}

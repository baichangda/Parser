package com.bcd.support_parser.processor;

public class ProcessContext<T> {
    public T instance;
    public final ProcessContext parentContext;

    public ProcessContext(T instance, ProcessContext parentContext) {
        this.instance = instance;
        this.parentContext = parentContext;
    }
}

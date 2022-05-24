package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.javassist.Parser;

public abstract class FieldBuilder {

    public static String varNameThis = "$0";
    public static String varNameByteBuf = "$1";
    public static String varNameParentProcessContext = "$2";
    public static String varNameParser = "parser";
    public Parser parser;

    public abstract void build(final BuilderContext context);

}

package com.bcd.parser.javassist.builder;


import com.bcd.parser.javassist.Parser;

public abstract class FieldBuilder {

    public Parser parser;

    public static String this_var_name = "$0";
    public static String byteBuf_var_name = "$1";
    public static String parentProcessContext_var_name = "$2";
    public static String parser_var_name = "parser";

    public abstract void build(BuilderContext context);
}

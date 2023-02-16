package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.javassist.Parser;

public abstract class FieldBuilder {
    public final static String varNameThis = "$0";
    public final static String varNameByteBuf = "$1";
    public final static String varNameParentProcessContext = "$2";

    public final static String varNameInstance = "instance";
    public final static String varNameParser = "parser";
    
    public Parser parser;

    public abstract void buildParse(final BuilderContext context);

    public void buildDeParse(final BuilderContext context){

    }

}

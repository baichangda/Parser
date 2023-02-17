package com.bcd.support_parser.builder;


import com.bcd.support_parser.Parser;

public abstract class FieldBuilder {
    public final static String varNameThis = "$0";
    public final static String varNameByteBuf = "$1";
    public final static String varNameParentProcessContext = "$2";

    public final static String varNameInstance = "_instance";
    public final static String varNameParser = "_parser";
    
    public Parser parser;

    public abstract void buildParse(final BuilderContext context);
    

    public void buildDeParse(final BuilderContext context){

    }

}

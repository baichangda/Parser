package com.bcd.support_parser.builder;


import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.anno.F_string;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FieldBuilder__F_string extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_string anno = field.getAnnotation(F_string.class);
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                lenRes = JavassistUtil.replaceVarToFieldName(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = FieldBuilder.varNameInstance;
        String arrVarName = varNameField + "_arr";
        String discardLenVarName = varNameField + "_discardLen";
        JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", arrVarName, lenRes);
        JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arrVarName);
        JavassistUtil.append(body, "final int {}=0;\n", discardLenVarName);
        JavassistUtil.append(body, "for(int i={}.length-1;i>=0;i--){\n", arrVarName);
        JavassistUtil.append(body, "if({}[i]==0){\n", arrVarName);
        JavassistUtil.append(body, "{}++;\n", discardLenVarName);
        JavassistUtil.append(body, "}else{\n", discardLenVarName);
        JavassistUtil.append(body, "break;\n", discardLenVarName);
        JavassistUtil.append(body, "}\n", discardLenVarName);
        JavassistUtil.append(body, "}\n", discardLenVarName);
        final Charset charset = Charset.forName(anno.charset());
        if (charset == StandardCharsets.UTF_8) {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{});\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName);
        } else if (charset == StandardCharsets.ISO_8859_1) {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{},java.nio.charset.StandardCharsets.ISO_8859_1);\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName);
        } else if (charset == StandardCharsets.UTF_16) {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{},java.nio.charset.StandardCharsets.UTF_16);\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName);
        } else if (charset == StandardCharsets.US_ASCII) {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{},java.nio.charset.StandardCharsets.US_ASCII);\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName);
        } else if (charset == StandardCharsets.UTF_16LE) {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{},java.nio.charset.StandardCharsets.UTF_16LE);\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName);
        } else if (charset == StandardCharsets.UTF_16BE) {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{},java.nio.charset.StandardCharsets.UTF_16BE);\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName);
        } else {
            JavassistUtil.append(body, "{}.{}=new String({},0,{}.length-{},java.nio.charset.Charset.forName(\"{}\"));\n", varNameInstance, field.getName(), arrVarName, arrVarName, discardLenVarName, charset.name());
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_string anno = field.getAnnotation(F_string.class);
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                lenRes = JavassistUtil.replaceVarToFieldName(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String valCode = varNameInstance + "." + field.getName();
        String arrVarName = varNameField + "_arr";
        String arrLeaveVarName = varNameField + "_leave";

        final Charset charset = Charset.forName(anno.charset());
        if (charset == StandardCharsets.UTF_8) {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes();\n", arrVarName, valCode, anno.charset());
        } else if (charset == StandardCharsets.ISO_8859_1) {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);\n", arrVarName, valCode, anno.charset());
        } else if (charset == StandardCharsets.UTF_16) {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes(java.nio.charset.StandardCharsets.UTF_16);\n", arrVarName, valCode, anno.charset());
        } else if (charset == StandardCharsets.US_ASCII) {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes(java.nio.charset.StandardCharsets.US_ASCII);\n", arrVarName, valCode, anno.charset());
        } else if (charset == StandardCharsets.UTF_16LE) {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes(java.nio.charset.StandardCharsets.UTF_16LE);\n", arrVarName, valCode, anno.charset());
        } else if (charset == StandardCharsets.UTF_16BE) {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes(java.nio.charset.StandardCharsets.UTF_16BE);\n", arrVarName, valCode, anno.charset());
        } else {
            JavassistUtil.append(body, "final byte[] {}={}.getBytes(java.nio.charset.Charset.forName(\"{}\"));\n", arrVarName, valCode, anno.charset());
        }


        JavassistUtil.append(body, "{}.writeBytes({});\n", varNameByteBuf, arrVarName);
        JavassistUtil.append(body, "final int {}={}-{}.length;\n", arrLeaveVarName, lenRes, arrVarName);
        JavassistUtil.append(body, "if({}>0){\n", arrLeaveVarName);
        JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", varNameByteBuf, arrLeaveVarName);
        JavassistUtil.append(body, "}\n");
    }
}

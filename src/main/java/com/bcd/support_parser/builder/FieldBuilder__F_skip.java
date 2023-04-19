package com.bcd.support_parser.builder;


import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_skip extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_skip anno = field.getAnnotation(F_skip.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);
        switch (anno.mode()) {
            case Skip -> {
                if (anno.len() == 0) {
                    if (anno.lenExpr().isEmpty()) {
                        throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
                    } else {
                        JavassistUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field));
                    }
                } else {
                    JavassistUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, anno.len());
                }
            }
            case Reserved -> {
                final String lenValCode;
                if (anno.len() == 0) {
                    if (anno.lenExpr().isEmpty()) {
                        throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
                    } else {
                        lenValCode = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
                    }
                } else {
                    lenValCode = anno.len() + "";
                }
                final String skipVarName = varNameField + "_skip";
                JavassistUtil.append(body, "int {}={}-{}.readerIndex()+{};\n", skipVarName,lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.startIndexVarName);
                JavassistUtil.append(body, "if({}>0){\n", skipVarName);
                JavassistUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skipVarName);
                JavassistUtil.append(body, "}\n");
            }
        }

    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_skip anno = field.getAnnotation(F_skip.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);
        switch (anno.mode()) {
            case Skip -> {
                if (anno.len() == 0) {
                    if (anno.lenExpr().isEmpty()) {
                        throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
                    } else {
                        JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field));
                    }
                } else {
                    JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, anno.len());
                }
            }
            case Reserved -> {
                final String lenValCode;
                if (anno.len() == 0) {
                    if (anno.lenExpr().isEmpty()) {
                        throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
                    } else {
                        lenValCode = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
                    }
                } else {
                    lenValCode = anno.len() + "";
                }
                final String skipVarName = varNameField + "_skip";
                JavassistUtil.append(body, "int {}={}-{}.writerIndex()+{};\n", skipVarName,lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.startIndexVarName);
                JavassistUtil.append(body, "if({}>0){\n", skipVarName);
                JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, skipVarName);
                JavassistUtil.append(body, "}\n");
            }
        }
    }
}

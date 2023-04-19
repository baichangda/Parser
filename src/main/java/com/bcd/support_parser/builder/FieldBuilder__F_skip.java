package com.bcd.support_parser.builder;


import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.JavassistUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;

public class FieldBuilder__F_skip extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_skip anno = field.getAnnotation(F_skip.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String lenValCode;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), fieldName, F_skip.class.getName());
            } else {
                lenValCode = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenValCode = anno.len() + "";
        }
        switch (anno.mode()) {
            case Skip -> {
                JavassistUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, lenValCode);
            }
            case ReservedFromStart -> {
                final String skipVarName = varNameField + "_skip";
                JavassistUtil.append(body, "final int {}={}-{}.readerIndex()+{};\n", skipVarName, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.startIndexVarName);
                JavassistUtil.append(body, "if({}>0){\n", skipVarName);
                JavassistUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skipVarName);
                JavassistUtil.append(body, "}\n");
                //完成后记录索引
                if (context.indexFieldNameSet.contains(fieldName)) {
                    final String indexVarName = varNameField + "_index";
                    JavassistUtil.append(body, "final int {}={}.readerIndex();\n", indexVarName, FieldBuilder.varNameByteBuf);
                    context.prevSkipReservedIndexVarName = indexVarName;
                }
            }
            case ReservedFromPrevReserved -> {
                final String skipVarName = varNameField + "_skip";
                JavassistUtil.append(body, "final int {}={}-{}.readerIndex()+{};\n", skipVarName, lenValCode, FieldBuilder.varNameByteBuf, context.prevSkipReservedIndexVarName);
                JavassistUtil.append(body, "if({}>0){\n", skipVarName);
                JavassistUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skipVarName);
                JavassistUtil.append(body, "}\n");
                //完成后记录索引
                if (context.indexFieldNameSet.contains(fieldName)) {
                    final String indexVarName = varNameField + "_index";
                    JavassistUtil.append(body, "final int {}={}.readerIndex();\n", indexVarName, FieldBuilder.varNameByteBuf);
                    context.prevSkipReservedIndexVarName = indexVarName;
                }
            }
        }

    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_skip anno = field.getAnnotation(F_skip.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String lenValCode;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), fieldName, F_skip.class.getName());
            } else {
                lenValCode = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenValCode = anno.len() + "";
        }
        switch (anno.mode()) {
            case Skip -> {
                JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, lenValCode);
            }
            case ReservedFromStart -> {
                final String skipVarName = varNameField + "_skip";
                JavassistUtil.append(body, "final int {}={}-{}.writerIndex()+{};\n", skipVarName, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.startIndexVarName);
                JavassistUtil.append(body, "if({}>0){\n", skipVarName);
                JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, skipVarName);
                JavassistUtil.append(body, "}\n");
                //完成后记录索引
                if (context.indexFieldNameSet.contains(fieldName)) {
                    final String indexVarName = varNameField + "_index";
                    JavassistUtil.append(body, "final int {}={}.writerIndex();\n", indexVarName, FieldBuilder.varNameByteBuf);
                    context.prevSkipReservedIndexVarName = indexVarName;
                }
            }
            case ReservedFromPrevReserved -> {
                final String skipVarName = varNameField + "_skip";
                JavassistUtil.append(body, "final int {}={}-{}.writerIndex()+{};\n", skipVarName, lenValCode, FieldBuilder.varNameByteBuf, context.prevSkipReservedIndexVarName);
                JavassistUtil.append(body, "if({}>0){\n", skipVarName);
                JavassistUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, skipVarName);
                JavassistUtil.append(body, "}\n");
                //完成后记录索引
                if (context.indexFieldNameSet.contains(fieldName)) {
                    final String indexVarName = varNameField + "_index";
                    JavassistUtil.append(body, "final int {}={}.writerIndex();\n", indexVarName, FieldBuilder.varNameByteBuf);
                    context.prevSkipReservedIndexVarName = indexVarName;
                }
            }
        }
    }
}

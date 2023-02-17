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

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_skip anno = field.getAnnotation(F_skip.class);
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
}

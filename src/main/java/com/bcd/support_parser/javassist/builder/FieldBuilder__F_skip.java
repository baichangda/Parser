package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.F_skip;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_skip extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_skip anno = field.getAnnotation(F_skip.class);
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                JavassistUtil.append(body, "{}.skip({});\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceVarToFieldName(anno.lenExpr(), context.varToFieldName, field));
            }
        } else {
            JavassistUtil.append(body, "{}.skip({});\n", FieldBuilder.varNameByteBuf, anno.len());
        }
    }
}

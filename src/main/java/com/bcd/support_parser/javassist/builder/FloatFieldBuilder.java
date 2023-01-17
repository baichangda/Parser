package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class FloatFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String varNameInstance = context.varNameInstance;
        String valExpr=null;
        switch (packetField.len()) {
            case 2 : {
                valExpr = JavassistUtil.format("(float){}.readUnsignedShort()", FieldBuilder.varNameByteBuf);
                break;
            }
            case 4 : {
                valExpr = JavassistUtil.format("(float){}.readInt()", FieldBuilder.varNameByteBuf);
                break;
            }
            default : {
                JavassistUtil.packetFieldLenNotSupport(field);
                break;
            }
        }
        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), JavassistUtil.replaceVarToValExpr(packetField.valExpr(),valExpr));
    }
}

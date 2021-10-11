package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class DoubleFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instance_var_name = context.instance_var_name;
        String valExpr=null;
        switch (packetField.len()) {
            case 4: {
                valExpr=JavassistUtil.format("(double){}.readUnsignedInt()",byteBuf_var_name);
                break;
            }
            case 8: {
                valExpr=JavassistUtil.format("(double){}.readLong()",byteBuf_var_name);
                break;
            }
            default: {
                JavassistUtil.packetField_len_notSupport(field);
            }
        }
        JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName, JavassistUtil.replace_var_to_valExpr(packetField.valExpr(),valExpr));
    }
}

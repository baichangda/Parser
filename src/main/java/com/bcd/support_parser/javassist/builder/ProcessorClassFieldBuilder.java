package com.bcd.support_parser.javassist.builder;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ProcessorClassFieldBuilder extends FieldBuilder {
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final Field field = context.field;
        final String processorClassVarName = JavassistUtil.toFirstLowerCase(packetField.processorClass().getSimpleName());
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        final String unBoxing = JavassistUtil.unBoxing(JavassistUtil.format("{}.process({},{})", processorClassVarName, FieldBuilder.varNameByteBuf, context.getClassProcessContextVarName()), field.getType());
        if(packetField.var()=='0'){
            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(),unBoxing);
        }else {
            JavassistUtil.append(body, "final {} {}={};\n",fieldTypeClassName,varNameField, unBoxing);
            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
            context.varToFieldName.put(packetField.var(), varNameField);
        }

    }
}

package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

public class ProcessorClassFieldBuilder extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String processorClassVarName = JavassistUtil.toFirstLowerCase(packetField.processorClass().getSimpleName());
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = context.field.getType().getName();
        final String unBoxing = JavassistUtil.unBoxing(JavassistUtil.format("{}.process({},{})", processorClassVarName, FieldBuilder.varNameByteBuf, context.getProcessorBuildContextVarName()), context.field.getType());
        if(packetField.var()=='0'){
            JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName,unBoxing);
        }else {
            JavassistUtil.append(body, "{} {}={};\n",fieldTypeClassName,varNameField, unBoxing);
            JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
            context.varToFieldName.put(packetField.var(), varNameField);
        }

    }
}

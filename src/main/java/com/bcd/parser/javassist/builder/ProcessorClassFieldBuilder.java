package com.bcd.parser.javassist.builder;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;

public class ProcessorClassFieldBuilder extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(context.field);
        final String processorClassVarName = JavassistUtil.toFirstLowerCase(packetField.processorClass().getSimpleName());
        final String instance_var_name = context.instance_var_name;
        final String fieldTypeClassName = context.field.getType().getName();
        if(context.processorBuildContextVarName ==null){
            context.processorBuildContextVarName = JavassistUtil.getVarName(context,"processContext");
            final String processContextClassName = FieldProcessContext.class.getName();
            JavassistUtil.append(body, "{} {}=new {}({},{},{});\n", processContextClassName, context.processorBuildContextVarName, processContextClassName, parser_var_name, instance_var_name, parentProcessContext_var_name);
        }
        final String unBoxing = JavassistUtil.unBoxing(JavassistUtil.format("{}.process({},{})", processorClassVarName, byteBuf_var_name, context.processorBuildContextVarName), context.field.getType());
        if(packetField.var()=='0'){
            JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName,unBoxing);
        }else {
            JavassistUtil.append(body, "{} {}={};\n",fieldTypeClassName,fieldVarName, unBoxing);
            JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName, fieldVarName);
            context.var_to_fieldName.put(packetField.var(), fieldVarName);
        }

    }
}

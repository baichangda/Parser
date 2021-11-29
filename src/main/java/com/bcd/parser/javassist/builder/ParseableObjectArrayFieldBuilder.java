package com.bcd.parser.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ParseableObjectArrayFieldBuilder extends FieldBuilder{
    @Override
    public void build(final BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final PacketField packetField = context.packetField;
        final String varNameInstance = context.varNameInstance;
        String listLenRes = JavassistUtil.replaceVarToFieldName(packetField.listLenExpr(), context.varToFieldName, field);
        final Class typeClass = field.getType().getComponentType();
        String typeClassName = typeClass.getName();
        JavassistUtil.append(body,"final {}[] {}=new {}[{}];\n",typeClassName,varNameField,typeClassName,listLenRes);
        if(parser.allInOne) {
            final String fieldVarNameTemp = varNameField + "_temp";
            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", listLenRes);
            JavassistUtil.append(body, "final {} {}=new {}();\n",typeClassName,fieldVarNameTemp,typeClassName);
            parser.buildAppend(body,typeClass,fieldVarNameTemp,context.parser,context);
            JavassistUtil.append(body, "{}[i]={};\n", varNameField, fieldVarNameTemp);
        }else{
            String processContextVarName = varNameField + "_processContext";
            final String processContextClassName = FieldProcessContext.class.getName();
            JavassistUtil.append(body, "final {} {}=new {}({},{},{});\n", processContextClassName, processContextVarName, processContextClassName, FieldBuilder.varNameParser, varNameInstance, FieldBuilder.varNameParentProcessContext);

            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", listLenRes);
            JavassistUtil.append(body,"{}[i]={}.parse({}.class,{},{});\n",varNameField, FieldBuilder.varNameParser,typeClassName, FieldBuilder.varNameByteBuf,processContextVarName);
        }
        JavassistUtil.append(body, "}\n");
        JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
    }
}

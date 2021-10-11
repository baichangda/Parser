package com.bcd.parser.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;

public class ParseableObjectArrayFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final PacketField packetField = context.packetField;
        final String instanceVarName = context.instanceVarName;
        String listLenRes = JavassistUtil.replaceVarToFieldName(packetField.listLenExpr(), context.varToFieldName, field);
        final Class typeClass = field.getType().getComponentType();
        String typeClassName = typeClass.getName();
        JavassistUtil.append(body,"{}[] {}=new {}[{}];\n",typeClassName,fieldVarName,typeClassName,listLenRes);
        if(parser.allInOne) {
            final String fieldVarNameTemp = fieldVarName + "_temp";
            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", listLenRes);
            JavassistUtil.append(body, "{} {}=new {}();\n",typeClassName,fieldVarNameTemp,typeClassName);
            parser.buildAppend(body,typeClass,fieldVarNameTemp,context.parser,context);
            JavassistUtil.append(body, "{}[i]={};\n", fieldVarName, fieldVarNameTemp);
        }else{
            String processContextVarName = fieldVarName + "_processContext";
            final String processContextClassName = FieldProcessContext.class.getName();
            JavassistUtil.append(body, "{} {}=new {}({},{},{});\n", processContextClassName, processContextVarName, processContextClassName, parser_var_name, instanceVarName, parentProcessContext_var_name);

            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", listLenRes);
            JavassistUtil.append(body,"{}[i]={}.parse({}.class,{},{});\n",fieldVarName,parser_var_name,typeClassName,byteBuf_var_name,processContextVarName);
        }
        JavassistUtil.append(body, "}\n");
        JavassistUtil.append(body, "{}.{}({});\n", instanceVarName, setMethodName, fieldVarName);
    }
}

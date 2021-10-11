package com.bcd.parser.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ParseableObjectListFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final PacketField packetField = context.packetField;
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instance_var_name = context.instance_var_name;
        String listLenRes = JavassistUtil.replace_var_to_fieldName(packetField.listLenExpr(), context.var_to_fieldName, field);
        final String arrayListClassName = ArrayList.class.getName();
        final String listClassName = List.class.getName();
        final Class typeClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String typeClassName = typeClass.getName();
        JavassistUtil.append(body, "{} {}=new {}({});\n", listClassName, fieldVarName, arrayListClassName, listLenRes);
        if(parser.allInOne) {
            final String fieldVarNameTemp = fieldVarName + "_temp";
            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", listLenRes);
            JavassistUtil.append(body, "{} {}=new {}();\n",typeClassName,fieldVarNameTemp,typeClassName);
            parser.buildAppend(body,typeClass,fieldVarNameTemp,context.parser,context);
            JavassistUtil.append(body, "{}.add({});\n", fieldVarName, fieldVarNameTemp);
        }else{
            String processContextVarName = fieldVarName + "_processContext";
            final String processContextClassName = FieldProcessContext.class.getName();
            JavassistUtil.append(body, "{} {}=new {}({},{},{});\n", processContextClassName, processContextVarName, processContextClassName, parser_var_name, instance_var_name, parentProcessContext_var_name);

            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", listLenRes);
            JavassistUtil.append(body, "{}.add({}.parse({}.class,{},{}));\n", fieldVarName, parser_var_name, typeClassName, byteBuf_var_name, processContextVarName);
        }
        JavassistUtil.append(body, "}\n");
        JavassistUtil.append(body, "{}.{}({});\n", instance_var_name, setMethodName, fieldVarName);
    }
}

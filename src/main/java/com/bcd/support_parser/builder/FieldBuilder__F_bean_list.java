package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_bean_list;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class FieldBuilder__F_bean_list extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final F_bean_list anno = context.field.getAnnotation(F_bean_list.class);
        final String fieldVarNameListLen = varNameField + "_listLen";
        if (anno.listLen() == 0) {
            String listLenRes = JavassistUtil.replaceLenExprToCode(anno.listLenExpr(), context.varToFieldName, field);
            JavassistUtil.append(body, "final int {}=(int)({});\n", fieldVarNameListLen, listLenRes);
        } else {
            JavassistUtil.append(body, "final int {}=(int)({});\n", fieldVarNameListLen, anno.listLen());
        }
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String arrayListClassName = ArrayList.class.getName();
        final String listClassName = List.class.getName();
        final Class typeClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String typeClassName = typeClass.getName();
        JavassistUtil.append(body, "final {} {}=new {}({});\n", listClassName, varNameField, arrayListClassName, fieldVarNameListLen);
        //在for循环外构造复用对象
        String processContextVarName = context.getProcessContextVarName();
        JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", fieldVarNameListLen);
        JavassistUtil.append(body, "{}.add({}.parse({}.class,{},{}));\n", varNameField, FieldBuilder.varNameParser, typeClassName, FieldBuilder.varNameByteBuf, processContextVarName);
        JavassistUtil.append(body, "}\n");
        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final Field field = context.field;
        final String listClassName = List.class.getName();
        final Class typeClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String typeClassName = typeClass.getName();
        JavassistUtil.append(body, "final {} {}={};\n", listClassName, varNameField, varNameInstance + "." + field.getName());
        //在for循环外构造复用对象
        String processContextVarName =  context.getProcessContextVarName();
        JavassistUtil.append(body, "for(int i=0;i<{}.size();i++){\n", varNameField);
        final String fieldVarNameTemp = varNameField + "_temp";
        JavassistUtil.append(body, "final {} {}=({}){}.get(i);\n", typeClassName, fieldVarNameTemp, typeClassName, varNameField);
        JavassistUtil.append(body, "{}.deParse({},{},{});\n", FieldBuilder.varNameParser, fieldVarNameTemp, FieldBuilder.varNameByteBuf, processContextVarName);
        JavassistUtil.append(body, "}\n");

    }
}

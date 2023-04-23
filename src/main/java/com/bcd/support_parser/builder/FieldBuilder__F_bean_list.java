package com.bcd.support_parser.builder;

import com.bcd.support_parser.Parser;
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
        final F_bean_list anno = context.field.getAnnotation(F_bean_list.class);
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final Class<?> typeClass;
        final int fieldTypeFlag;
        if (fieldType.isArray()) {
            fieldTypeFlag = 1;
            typeClass = fieldType.getComponentType();
        } else if (List.class.isAssignableFrom(fieldType)) {
            fieldTypeFlag = 2;
            typeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else {
            JavassistUtil.notSupport_type(field, anno.getClass());
            fieldTypeFlag = 0;
            typeClass = null;
        }
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String parserClassName = Parser.class.getName();
        final String fieldVarNameListLen = varNameField + "_listLen";
        if (anno.listLen() == 0) {
            String listLenRes = JavassistUtil.replaceLenExprToCode(anno.listLenExpr(), context.varToFieldName, field);
            JavassistUtil.append(body, "final int {}=(int)({});\n", fieldVarNameListLen, listLenRes);
        } else {
            JavassistUtil.append(body, "final int {}=(int)({});\n", fieldVarNameListLen, anno.listLen());
        }

        final String typeClassName = typeClass.getName();
        switch (fieldTypeFlag) {
            case 1 -> {
                JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", typeClassName, varNameField, typeClassName, fieldVarNameListLen);
                //在for循环外构造复用对象
                String processContextVarName = context.getProcessContextVarName();
                JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", fieldVarNameListLen);
                JavassistUtil.append(body, "{}[i]={}.parse({}.class,{},{});\n", varNameField, parserClassName, typeClassName, FieldBuilder.varNameByteBuf, processContextVarName);
                JavassistUtil.append(body, "}\n");
            }
            case 2 -> {
                final String arrayListClassName = ArrayList.class.getName();
                final String listClassName = List.class.getName();
                JavassistUtil.append(body, "final {} {}=new {}({});\n", listClassName, varNameField, arrayListClassName, fieldVarNameListLen);
                //在for循环外构造复用对象
                String processContextVarName = context.getProcessContextVarName();
                JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", fieldVarNameListLen);
                JavassistUtil.append(body, "{}.add({}.parse({}.class,{},{}));\n", varNameField, parserClassName, typeClassName, FieldBuilder.varNameByteBuf, processContextVarName);
                JavassistUtil.append(body, "}\n");
            }
        }
        JavassistUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), varNameField);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final Field field = context.field;
        final F_bean_list anno = context.field.getAnnotation(F_bean_list.class);
        final String fieldName = field.getName();
        final String valCode = FieldBuilder.varNameInstance + "." + fieldName;

        JavassistUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);

        final Class<?> fieldType = field.getType();
        final Class<?> typeClass;
        final int fieldTypeFlag;
        if (fieldType.isArray()) {
            fieldTypeFlag = 1;
            typeClass = fieldType.getComponentType();
        } else if (List.class.isAssignableFrom(fieldType)) {
            fieldTypeFlag = 2;
            typeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else {
            JavassistUtil.notSupport_type(field, anno.getClass());
            fieldTypeFlag = 0;
            typeClass = null;
        }

        final String typeClassName = typeClass.getName();
        final String parserClassName = Parser.class.getName();
        final String processContextVarName = context.getProcessContextVarName();
        final String fieldVarNameTemp = varNameField + "_temp";
        switch (fieldTypeFlag) {
            case 1 -> {
                JavassistUtil.append(body, "final {}[] {}={};\n", typeClassName, varNameField, valCode);
                //在for循环外构造复用对象
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameField);
                JavassistUtil.append(body, "final {} {}=({}){}[i];\n", typeClassName, fieldVarNameTemp, typeClassName, varNameField);
            }
            case 2 -> {
                final String listClassName = List.class.getName();
                JavassistUtil.append(body, "final {} {}={};\n", listClassName, varNameField, valCode);
                //在for循环外构造复用对象
                JavassistUtil.append(body, "for(int i=0;i<{}.size();i++){\n", varNameField);
                JavassistUtil.append(body, "final {} {}=({}){}.get(i);\n", typeClassName, fieldVarNameTemp, typeClassName, varNameField);
            }
        }
        JavassistUtil.append(body, "{}.deParse({},{},{});\n", parserClassName, fieldVarNameTemp, FieldBuilder.varNameByteBuf, processContextVarName);
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "}\n");
    }
}

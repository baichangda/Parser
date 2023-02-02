package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.anno.F_bean_list;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class FieldBuilder__F_bean_list extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final F_bean_list anno = context.field.getAnnotation(F_bean_list.class);
        final String fieldVarNameListLen = varNameField + "_listLen";
        if(anno.listLen()==0){
            String listLenRes = JavassistUtil.replaceVarToFieldName(anno.listLenExpr(), context.varToFieldName, field);
            JavassistUtil.append(body, "final int {}=(int)({});\n", fieldVarNameListLen,listLenRes);
        }else{
            JavassistUtil.append(body, "final int {}=(int)({});\n", fieldVarNameListLen,anno.listLen());
        }
        final String varNameInstance = context.varNameInstance;
        final String arrayListClassName = ArrayList.class.getName();
        final String listClassName = List.class.getName();
        final Class typeClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String typeClassName = typeClass.getName();
        JavassistUtil.append(body, "final {} {}=new {}({});\n", listClassName, varNameField, arrayListClassName, fieldVarNameListLen);
        if(parser.allInOne) {
            final String fieldVarNameTemp = varNameField + "_temp";
            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", fieldVarNameListLen);
            JavassistUtil.append(body, "final {} {}=new {}();\n",typeClassName,fieldVarNameTemp,typeClassName);
            parser.buildAppend(body,typeClass,fieldVarNameTemp,context.parser,context);
            JavassistUtil.append(body, "{}.add({});\n", varNameField, fieldVarNameTemp);
        }else{
            final String classProcessContextVarName = context.getClassProcessContextVarName();
            JavassistUtil.append(body, "for(int i=0;i<{};i++){\n", fieldVarNameListLen);
            JavassistUtil.append(body, "{}.add({}.parse({}.class,{},{}));\n", varNameField, FieldBuilder.varNameParser, typeClassName, FieldBuilder.varNameByteBuf, classProcessContextVarName);
        }
        JavassistUtil.append(body, "}\n");
        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
    }
}

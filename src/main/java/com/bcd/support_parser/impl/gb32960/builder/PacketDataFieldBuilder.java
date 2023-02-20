package com.bcd.support_parser.impl.gb32960.builder;

import com.bcd.support_parser.Parser;
import com.bcd.support_parser.impl.gb32960.data.*;
import com.bcd.support_parser.builder.BuilderContext;
import com.bcd.support_parser.builder.FieldBuilder;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;


public class PacketDataFieldBuilder extends FieldBuilder {

    @Override
    public void buildParse(final BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        final String parserClassName = Parser.class.getName();

        JavassistUtil.append(body, "{} {}=null;\n", fieldTypeClassName, varNameField);

        final String parseContextVarName = context.getProcessContextVarName();

        JavassistUtil.append(body, "switch ({}.flag){\n", varNameInstance);
        JavassistUtil.append(body, "case 1:{\n", varNameInstance);
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleLoginData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 2:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleRealData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 3:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleSupplementData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 4:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleLogoutData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 5:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, PlatformLoginData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 6:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, PlatformLogoutData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 7:{\n");
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 8:{\n");
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        super.buildDeParse(context);
    }
}

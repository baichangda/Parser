package com.bcd.parser.impl.gb32960.javassist.builder;

import com.bcd.parser.impl.gb32960.data.*;
import com.bcd.parser.javassist.builder.BuilderContext;
import com.bcd.parser.javassist.builder.FieldBuilder;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;


public class PacketDataFieldBuilder extends FieldBuilder {

    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String varNameInstance = context.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();

        JavassistUtil.append(body, "{} {}=null;\n", fieldTypeClassName, varNameField);

        final String processorBuildContextVarName = context.getProcessorBuildContextVarName();

        JavassistUtil.append(body, "switch ({}.getFlag()){\n", varNameInstance);
        JavassistUtil.append(body, "case 1:{\n", varNameInstance);
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, FieldBuilder.varNameParser, VehicleLoginData.class.getName(), FieldBuilder.varNameByteBuf, processorBuildContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 2:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, FieldBuilder.varNameParser, VehicleRealData.class.getName(), FieldBuilder.varNameByteBuf, processorBuildContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 3:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, FieldBuilder.varNameParser, VehicleSupplementData.class.getName(), FieldBuilder.varNameByteBuf, processorBuildContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 4:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, FieldBuilder.varNameParser, VehicleLogoutData.class.getName(), FieldBuilder.varNameByteBuf, processorBuildContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 5:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, FieldBuilder.varNameParser, PlatformLoginData.class.getName(), FieldBuilder.varNameByteBuf, processorBuildContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 6:{\n");
        JavassistUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, FieldBuilder.varNameParser, PlatformLogoutData.class.getName(), FieldBuilder.varNameByteBuf, processorBuildContextVarName);
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 7:{\n");
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "case 8:{\n");
        JavassistUtil.append(body, "break;\n");
        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "}\n");

        JavassistUtil.append(body, "{}.{}({});\n", varNameInstance, setMethodName, varNameField);
    }

}

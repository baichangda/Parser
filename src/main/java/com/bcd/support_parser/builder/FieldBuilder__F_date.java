package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_date;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;
import java.time.*;
import java.util.Date;

public class FieldBuilder__F_date extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date anno = context.field.getAnnotation(F_date.class);
        final int baseYear = anno.baseYear();
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String zoneIdClassName = ZoneId.class.getName();
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            final String dateClassName = Date.class.getName();
            JavassistUtil.append(body, "{}.{}={}.from({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}.of(\"{}\")).toInstant());\n",
                    varNameInstance, field.getName(), dateClassName, zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdClassName, anno.zoneId());
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "{}.{}={}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}.of(\"{}\")).toInstant().toEpochMilli();\n",
                    varNameInstance, field.getName(), zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdClassName, anno.zoneId());
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "{}.{}=(int)({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}.of(\"{}\")).toInstant().getEpochSecond());\n",
                    varNameInstance, field.getName(), zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdClassName, anno.zoneId());
        } else {
            JavassistUtil.notSupport_fieldType(field, F_date.class);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date anno = context.field.getAnnotation(F_date.class);
        final int baseYear = anno.baseYear();
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = varNameInstance + "." + field.getName();
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String zoneIdClassName = ZoneId.class.getName();
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String fieldLocalDateTimeVarName = fieldVarName + "_localDateTime";
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final {} {}={}.ofInstant({}.toInstant(), {}.of(\"{}8\"));\n", zoneDateTimeClassName, fieldLocalDateTimeVarName, zoneDateTimeClassName, valCode, zoneIdClassName,anno.zoneId());
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            final String instantClassName = Instant.class.getName();
            JavassistUtil.append(body, "final {} {}={}.ofInstant({}.ofEpochMilli({}), {}.of(\"{}\"));\n", zoneDateTimeClassName, fieldLocalDateTimeVarName, zoneDateTimeClassName, instantClassName, valCode,zoneIdClassName, anno.zoneId());
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            final String instantClassName = Instant.class.getName();
            JavassistUtil.append(body, "final {} {}={}.ofInstant({}.ofEpochSecond((long){}), {}.of(\"{}\"));\n", zoneDateTimeClassName, fieldLocalDateTimeVarName, zoneDateTimeClassName, instantClassName, valCode,zoneIdClassName, anno.zoneId());
        } else {
            JavassistUtil.notSupport_fieldType(field, F_date.class);
        }
        JavassistUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getYear() - {}),(byte){}.getMonth().getValue(),(byte){}.getDayOfMonth(),(byte){}.getHour(),(byte){}.getMinute(),(byte){}.getSecond()});\n",
                varNameByteBuf, fieldLocalDateTimeVarName, baseYear, fieldLocalDateTimeVarName, fieldLocalDateTimeVarName, fieldLocalDateTimeVarName, fieldLocalDateTimeVarName, fieldLocalDateTimeVarName);
    }
}

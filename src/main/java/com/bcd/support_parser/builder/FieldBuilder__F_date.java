package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_date;
import com.bcd.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String zoneIdVarName = JavassistUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            final String dateClassName = Date.class.getName();
            JavassistUtil.append(body, "{}.{}={}.from({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant());\n",
                    varNameInstance, field.getName(), dateClassName, zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdVarName);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "{}.{}={}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                    varNameInstance, field.getName(), zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdVarName);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "{}.{}=(int)({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().getEpochSecond());\n",
                    varNameInstance, field.getName(), zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdVarName);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String dateTimeFormatterVarName = JavassistUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.formatWhenString(), zoneIdVarName);
            JavassistUtil.append(body, "{}.{}=({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{})).format({});\n",
                    varNameInstance, field.getName(), zoneDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneIdVarName, dateTimeFormatterVarName);
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
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = FieldBuilder.varNameInstance + "." + field.getName();
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String fieldVarName = JavassistUtil.getFieldVarName(context);
        final String fieldZoneDateTimeVarName = fieldVarName + "_zoneDateTime";
        final String zoneIdVarName = JavassistUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final {} {}={}.ofInstant({}.toInstant(), {});\n", zoneDateTimeClassName, fieldZoneDateTimeVarName, zoneDateTimeClassName, valCode, zoneIdVarName);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            final String instantClassName = Instant.class.getName();
            JavassistUtil.append(body, "final {} {}={}.ofInstant({}.ofEpochMilli({}), {});\n", zoneDateTimeClassName, fieldZoneDateTimeVarName, zoneDateTimeClassName, instantClassName, valCode, zoneIdVarName);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            final String instantClassName = Instant.class.getName();
            JavassistUtil.append(body, "final {} {}={}.ofInstant({}.ofEpochSecond((long){}), {});\n", zoneDateTimeClassName, fieldZoneDateTimeVarName, zoneDateTimeClassName, instantClassName, valCode, zoneIdVarName);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String dateTimeFormatterVarName = JavassistUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.formatWhenString(), zoneIdVarName);
            JavassistUtil.append(body, "final {} {}={}.parse({},{});\n", zoneDateTimeClassName, fieldZoneDateTimeVarName, zoneDateTimeClassName, valCode, dateTimeFormatterVarName);
        } else {
            JavassistUtil.notSupport_fieldType(field, F_date.class);
        }
        JavassistUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getYear() - {}),(byte){}.getMonth().getValue(),(byte){}.getDayOfMonth(),(byte){}.getHour(),(byte){}.getMinute(),(byte){}.getSecond()});\n",
                varNameByteBuf, fieldZoneDateTimeVarName, baseYear, fieldZoneDateTimeVarName, fieldZoneDateTimeVarName, fieldZoneDateTimeVarName, fieldZoneDateTimeVarName, fieldZoneDateTimeVarName);
    }
}

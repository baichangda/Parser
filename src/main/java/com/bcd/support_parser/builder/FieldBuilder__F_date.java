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
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String varNameZoneId = JavassistUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.order);
        //先转换为毫秒
        switch (anno.mode()) {
            case Bytes_yyMMddHHmmss -> {
                JavassistUtil.append(body, "final long {}={}.of({}+{}.readUnsignedByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                        varNameLongField, zoneDateTimeClassName, anno.baseYear()
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, varNameZoneId);
            }
            case Bytes_yyyyMMddHHmmss -> {
                JavassistUtil.append(body, "final long {}={}.of({}.readUnsignedByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                        varNameLongField, zoneDateTimeClassName
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, varNameZoneId);
            }
            case Uint64_millisecond -> {
                final String readFuncName = bigEndian ? "readLong" : "readLongLE";
                JavassistUtil.append(body, "final long {}={}.{}();\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case Uint64_second -> {
                final String readFuncName = bigEndian ? "readLong" : "readLongLE";
                JavassistUtil.append(body, "final long {}={}.{}()*1000;\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case Uint32_second -> {
                final String readFuncName = bigEndian ? "readUnsignedInt" : "readUnsignedIntLE";
                JavassistUtil.append(body, "final long {}={}.{}()*1000;\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case Float64_millisecond -> {
                final String readFuncName = bigEndian ? "readDouble" : "readDoubleLE";
                JavassistUtil.append(body, "final long {}=(long){}.{}();\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case Float64_second -> {
                final String readFuncName = bigEndian ? "readDouble" : "readDoubleLE";
                JavassistUtil.append(body, "final long {}=(long)({}.{}()*1000);\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
        }
        //根据字段类型格式化
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            final String dateClassName = Date.class.getName();
            JavassistUtil.append(body, "{}.{}=new {}({});\n", varNameInstance, field.getName(), dateClassName, varNameLongField);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameLongField);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "{}.{}=(int)({}/1000);\n", varNameInstance, field.getName(), varNameLongField);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameStringZoneId = JavassistUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.stringZoneId());
            final String dateTimeFormatterVarName = JavassistUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.stringFormat(), varNameStringZoneId);
            JavassistUtil.append(body, "{}.{}={}.ofInstant({}.ofEpochMilli({}),{}).format({});\n",
                    varNameInstance,
                    field.getName(),
                    zoneDateTimeClassName,
                    Instant.class.getName(),
                    varNameLongField,
                    varNameStringZoneId,
                    dateTimeFormatterVarName);
        } else {
            JavassistUtil.notSupport_fieldType(field, F_date.class);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date anno = context.field.getAnnotation(F_date.class);
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = FieldBuilder.varNameInstance + "." + field.getName();
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameZoneId = JavassistUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.order);
        //根据字段类型获取long
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final long {}={}.getTime();\n", varNameLongField, valCode);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final long {}={};\n", varNameLongField, valCode);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            JavassistUtil.append(body, "final long {}=(long)({})*1000L;\n", varNameLongField, valCode);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameStringZoneId = JavassistUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.stringZoneId());
            final String dateTimeFormatterVarName = JavassistUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.stringFormat(), varNameStringZoneId);
            JavassistUtil.append(body, "final long {}={}.parse({},{}).toInstant().toEpochMilli();\n", varNameLongField,zoneDateTimeClassName, valCode, dateTimeFormatterVarName);
        } else {
            JavassistUtil.notSupport_fieldType(field, F_date.class);
        }

        //先转换为毫秒
        switch (anno.mode()) {
            case Bytes_yyMMddHHmmss -> {
                final String varNameZoneDateTimeField = varNameField + "zoneDateTime";
                JavassistUtil.append(body, "{} {}={}.ofInstant({}.ofEpochMilli({}),{});\n",
                        zoneDateTimeClassName,
                        varNameZoneDateTimeField,
                        zoneDateTimeClassName,
                        Instant.class.getName(),
                        varNameLongField,
                        varNameZoneId);
                JavassistUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getYear()-{}),(byte)({}.getMonthValue()),(byte)({}.getDayOfMonth()),(byte)({}.getHour()),(byte)({}.getMinute()),(byte)({}.getSecond())});\n",
                        FieldBuilder.varNameByteBuf,
                        varNameZoneDateTimeField,
                        anno.baseYear(),
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField);
            }
            case Bytes_yyyyMMddHHmmss -> {
                final String writeFuncName = bigEndian ? "writeShort" : "writeShortLE";
                final String varNameZoneDateTimeField = varNameField + "zoneDateTime";
                JavassistUtil.append(body, "{} {}={}.ofInstant({}.ofEpochMilli({}),{});\n",
                        zoneDateTimeClassName,
                        varNameZoneDateTimeField,
                        zoneDateTimeClassName,
                        Instant.class.getName(),
                        varNameLongField,
                        varNameZoneId);
                JavassistUtil.append(body, "{}.{}((short){}.getYear());\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameZoneDateTimeField);
                JavassistUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getMonthValue()),(byte)({}.getDayOfMonth()),(byte)({}.getHour()),(byte)({}.getMinute()),(byte)({}.getSecond())});\n",
                        FieldBuilder.varNameByteBuf,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField);
            }
            case Uint64_millisecond -> {
                final String writeFuncName = bigEndian ? "writeLong" : "writeLongLE";
                JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case Uint64_second -> {
                final String writeFuncName = bigEndian ? "writeLong" : "writeLongLE";
                JavassistUtil.append(body, "{}.{}({}/1000L);\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case Uint32_second -> {
                final String writeFuncName = bigEndian ? "writeInt" : "writeIntLE";
                JavassistUtil.append(body, "{}.{}((int)({}/1000L));\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case Float64_millisecond -> {
                final String writeFuncName = bigEndian ? "writeDouble" : "writeDoubleLE";
                JavassistUtil.append(body, "{}.{}((double)({}));\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case Float64_second -> {
                final String writeFuncName = bigEndian ? "writeDouble" : "writeDoubleLE";
                JavassistUtil.append(body, "{}.{}((double){}/1000d);\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
        }
    }
}

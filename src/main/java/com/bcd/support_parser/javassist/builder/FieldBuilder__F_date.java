package com.bcd.support_parser.javassist.builder;

import com.bcd.support_parser.anno.F_date;
import com.bcd.support_parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class FieldBuilder__F_date extends FieldBuilder {
    @Override
    public void build(BuilderContext context) {

        final StringBuilder body = context.body;
        final F_date anno = context.field.getAnnotation(F_date.class);
        final int baseYear = anno.baseYear();
        final Field field = context.field;
        final String varNameInstance = context.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            final String dateClassName = Date.class.getName();
            final String localDateTimeClassName = LocalDateTime.class.getName();
            final String zoneOffsetClassName = ZoneOffset.class.getName();
            JavassistUtil.append(body, "{}.{}={}.from({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte()).toInstant({}.of(\"+8\")));\n",
                    varNameInstance, field.getName(), dateClassName, localDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneOffsetClassName);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            final String localDateTimeClassName = LocalDateTime.class.getName();
            final String zoneOffsetClassName = ZoneOffset.class.getName();
            JavassistUtil.append(body, "{}.{}={}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte()).toInstant({}.of(\"+8\")).getEpochSecond();\n",
                    varNameInstance, field.getName(), localDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneOffsetClassName);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            final String localDateTimeClassName = LocalDateTime.class.getName();
            final String zoneOffsetClassName = ZoneOffset.class.getName();
            JavassistUtil.append(body, "{}.{}=(int)({}.of({}+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte()).toInstant({}.of(\"+8\")).toEpochMilli());\n",
                    varNameInstance, field.getName(), localDateTimeClassName, baseYear
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                    , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, zoneOffsetClassName);
        } else {
            JavassistUtil.notSupport_fieldType(field, F_date.class);
        }
    }
}

package com.bcd.parser.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.util.JavassistUtil;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateFieldBuilder extends FieldBuilder{
    @Override
    public void build(BuilderContext context) {
        final StringBuilder body = context.body;
        final PacketField packetField = context.packetField;
        final Field field = context.field;
        final String setMethodName = JavassistUtil.getSetMethodName(field);
        final String instanceVarName = context.instanceVarName;
        if(packetField.len()==6){
            final String dateClassName = Date.class.getName();
            final String localDateTimeClassName = LocalDateTime.class.getName();
            final String zoneOffsetClassName = ZoneOffset.class.getName();
            JavassistUtil.append(body,"{}.{}({}.from({}.of(2000+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte()).toInstant({}.of(\"+8\"))));\n",
                    instanceVarName,setMethodName,dateClassName,localDateTimeClassName
                    ,byteBuf_var_name,byteBuf_var_name,byteBuf_var_name,byteBuf_var_name
            ,byteBuf_var_name,byteBuf_var_name,zoneOffsetClassName);
        }else{
            JavassistUtil.packetFieldLenNotSupport(field);
        }


    }
}

package com.bcd.parser.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.exception.BaseRuntimeException;
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
        final String instance_var_name = context.instance_var_name;
        if(packetField.len()==6){
            final String date_class_name = Date.class.getName();
            final String localDateTime_class_name = LocalDateTime.class.getName();
            final String zoneOffset_class_name = ZoneOffset.class.getName();
            JavassistUtil.append(body,"{}.{}({}.from({}.of(2000+{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte()).toInstant({}.of(\"+8\"))));\n",
                    instance_var_name,setMethodName,date_class_name,localDateTime_class_name
                    ,byteBuf_var_name,byteBuf_var_name,byteBuf_var_name,byteBuf_var_name
            ,byteBuf_var_name,byteBuf_var_name,zoneOffset_class_name);
        }else{
            JavassistUtil.packetField_len_notSupport(field);
        }


    }
}

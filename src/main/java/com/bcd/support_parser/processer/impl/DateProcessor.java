package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 解析{@link Date}类型字段
 */
public class DateProcessor extends FieldProcessor<Date> {
    private final static int BASE_YEAR=2000;

    private final static ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");

    @Override
    public Date process(ByteBuf data, FieldProcessContext processContext){
        if(processContext.len==6){
            byte year=data.readByte();
            int month=data.readByte();
            int day=data.readByte();
            int hour=data.readByte();
            int minute=data.readByte();
            int second=data.readByte();
            return Date.from(LocalDateTime.of(BASE_YEAR+year,month,day,hour,minute,second).toInstant(ZONE_OFFSET));
        }else{
            throw BaseRuntimeException.getException("date length must be 6,actual "+processContext.len);
        }
    }

    @Override
    public void deProcess(Date data, ByteBuf dest, FieldDeProcessContext processContext) {
        if(processContext.len==6){
            LocalDateTime ldt= LocalDateTime.ofInstant(data.toInstant(), ZONE_OFFSET);
            dest.writeByte(ldt.getYear()-BASE_YEAR);
            dest.writeByte(ldt.getMonthValue());
            dest.writeByte(ldt.getDayOfMonth());
            dest.writeByte(ldt.getHour());
            dest.writeByte(ldt.getMinute());
            dest.writeByte(ldt.getSecond());
        }else{
            throw BaseRuntimeException.getException("date length must be 6,actual "+processContext.len);
        }
    }
}

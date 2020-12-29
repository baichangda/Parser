package com.bcd.parser.process.impl;

import com.bcd.parser.anno.Parsable;
import com.bcd.parser.process.FieldDeProcessContext;
import com.bcd.parser.process.FieldProcessContext;
import com.bcd.parser.process.FieldProcessor;
import io.netty.buffer.ByteBuf;

/**
 * 解析{@link Parsable}标注的类
 * 作为默认的实体类解析器
 */
@SuppressWarnings("unchecked")
public class ParsableObjectProcessor  extends FieldProcessor<Object> {
    @Override
    public Object process(ByteBuf data, FieldProcessContext processContext) {
        return parser.parse(processContext.getFieldInfo().getClazz(),data);
    }

    @Override
    public void deProcess(Object data, ByteBuf dest, FieldDeProcessContext processContext) {
        parser.deParse(data,dest,processContext);
    }
}

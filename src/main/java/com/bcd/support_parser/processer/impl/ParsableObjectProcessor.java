package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.anno.Parsable;
import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

/**
 * 解析{@link Parsable}标注的类
 * 作为默认的实体类解析器
 */
@SuppressWarnings("unchecked")
public class ParsableObjectProcessor  extends FieldProcessor<Object> {
    @Override
    public Object process(ByteBuf data, FieldProcessContext processContext) {
        return parser.parse(processContext.fieldInfo.clazz,data);
    }

    @Override
    public void deProcess(Object data, ByteBuf dest, FieldDeProcessContext processContext) {
        parser.deParse(data,dest,processContext);
    }
}

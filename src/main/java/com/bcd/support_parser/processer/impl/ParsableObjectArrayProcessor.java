package com.bcd.support_parser.processer.impl;

import com.bcd.support_parser.anno.Parsable;
import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 解析{@link Parsable}标注的类
 * 作为默认的实体类解析器
 */
@SuppressWarnings("unchecked")
public class ParsableObjectArrayProcessor extends FieldProcessor<Object> {
    @Override
    public Object process(ByteBuf data, FieldProcessContext processContext) {
        int listLen=processContext.listLen;
        Class arrayType= processContext.fieldInfo.clazz;
        Object arr= Array.newInstance(arrayType,listLen);
        for (int i = 0; i < listLen; i++) {
            Array.set(arr,i,parser.parse(arrayType,data,processContext));
        }
        return arr;
    }

    @Override
    public void deProcess(Object data, ByteBuf dest, FieldDeProcessContext processContext) {
        int listLen=Array.getLength(data);
        for (int i = 0; i < listLen; i++) {
            parser.deParse(Array.get(data,i),dest,processContext);
        }
    }
}

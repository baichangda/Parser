package com.bcd.parser.processer.impl;

import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析{@link List}类型字段
 */
@SuppressWarnings("unchecked")
public class ParsableObjectListProcessor extends FieldProcessor<List> {

    @Override
    public List process(ByteBuf data, FieldProcessContext processContext) {
        int listLen=processContext.listLen;
        List list=new ArrayList(listLen);
        for (int i = 0; i < listLen; i++) {
            list.add(parser.parse(processContext.fieldInfo.clazz,data,processContext));
        }
        return list;
    }

    @Override
    public void deProcess(List data, ByteBuf dest, FieldDeProcessContext processContext) {
        for (Object o : data) {
            parser.deParse(o,dest,processContext);
        }
    }
}

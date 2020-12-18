package com.bcd.parser.process.impl;

import com.bcd.parser.process.FieldDeProcessContext;
import com.bcd.parser.process.FieldProcessContext;
import com.bcd.parser.process.FieldProcessor;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析{@link List}类型字段
 */
@SuppressWarnings("unchecked")
public class ListProcessor extends FieldProcessor<List> {

    @Override
    public List process(ByteBuf data, FieldProcessContext processContext) {
        int listLen=processContext.getListLen();
        List list=new ArrayList(listLen);
        for (int i = 0; i < listLen; i++) {
            list.add(parser.parse(processContext.getFieldInfo().getClazz(),data,processContext));
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

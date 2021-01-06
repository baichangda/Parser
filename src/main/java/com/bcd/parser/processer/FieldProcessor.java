package com.bcd.parser.processer;


import com.bcd.parser.Parser;
import com.bcd.parser.anno.PacketField;
import com.bcd.parser.exception.BaseRuntimeException;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class FieldProcessor<T> {
    protected Logger logger= LoggerFactory.getLogger(this.getClass());

    protected Parser parser;

    public void setParser(Parser parser){
        this.parser = parser;
    }

    public Parser getParser() {
        return parser;
    }

    /**
     * 读取byteBuf数据转换成对象
     * @param data
     * @param processContext
     * @return
     */
    public T process(ByteBuf data, FieldProcessContext processContext){
        throw BaseRuntimeException.getException("process not support");
    }

    /**
     * 解析对象转换为byteBuf
     * @param data
     * @param dest
     * @param processContext
     */
    public void deProcess(T data, ByteBuf dest, FieldDeProcessContext processContext){
        throw BaseRuntimeException.getException("deProcess not support");
    }

}

package com.bcd.parser.process;


import com.bcd.parser.Parser;
import com.bcd.parser.anno.PacketField;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.info.FieldInfo;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.Objects;

public abstract class FieldProcessor<T> {
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

    /**
     * 验证{@link PacketField#valExpr()}是否为空
     * @param processContext
     */
    protected void checkValRpnNull(FieldDeProcessContext processContext){
        if(processContext.getFieldInfo().getValRpn()!=null){
            Field field=processContext.getFieldInfo().getField();
            throw BaseRuntimeException.getException("class[{0}] field[{1}] has valExpr,deProcess not support",
                    field.getDeclaringClass().getName(),
                    field.getName());
        }
    }

}

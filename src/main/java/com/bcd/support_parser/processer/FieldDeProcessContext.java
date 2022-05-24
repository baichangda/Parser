package com.bcd.parser.processer;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.info.FieldInfo;

public class FieldDeProcessContext {
    /**
     * 字段信息
     */
    public FieldInfo fieldInfo;

    /**
     * 字段值占用字节长度
     * 取自
     * {@link PacketField#len()}、{@link PacketField#lenExpr()}
     */
    public int len;

    /**
     * 集合长度,只有List类型时候才有效
     * 取自
     * {@link PacketField#listLenExpr()}}
     */
    public int listLen;

    /**
     * 字段所属实例
     */
    public Object instance;

    /**
     * 当前字段所属对象所属字段的 环境变量
     * 即父环境
     * 如果是顶级,则为null
     */
    FieldDeProcessContext parentContext;

    public FieldDeProcessContext(Object instance, FieldDeProcessContext parentContext) {
        this.instance = instance;
        this.parentContext = parentContext;
    }

    public FieldDeProcessContext() {
    }

}

package com.bcd.parser.info;



import com.bcd.parser.anno.PacketField;

import java.util.ArrayList;

public class PacketInfo {
    private Class clazz;

    //解析的字段信息集合
    private FieldInfo[] fieldInfos;
    //计算偏移量字段集合
    private OffsetFieldInfo[] offsetFieldInfos;

    /**
     * 类中{@link PacketField#var()}属性存在的字段个数
     */
    public int varValArrLen=0;

    /**
     * 类中{@link PacketField#var()}属性存在的字段中最小char(以char对应int来排序)对应的int
     */
    public int varValArrOffset=0;

    public PacketInfo() {
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public FieldInfo[] getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(FieldInfo[] fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public OffsetFieldInfo[] getOffsetFieldInfos() {
        return offsetFieldInfos;
    }

    public void setOffsetFieldInfos(OffsetFieldInfo[] offsetFieldInfos) {
        this.offsetFieldInfos = offsetFieldInfos;
    }

    public int getVarValArrLen() {
        return varValArrLen;
    }

    public void setVarValArrLen(int varValArrLen) {
        this.varValArrLen = varValArrLen;
    }

    public int getVarValArrOffset() {
        return varValArrOffset;
    }

    public void setVarValArrOffset(int varValArrOffset) {
        this.varValArrOffset = varValArrOffset;
    }
}

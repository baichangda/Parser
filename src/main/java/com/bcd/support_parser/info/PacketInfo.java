package com.bcd.support_parser.info;



import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.exception.BaseRuntimeException;

import java.lang.reflect.Constructor;

public final class PacketInfo {
    //对应的class
    public final Class clazz;

    //对应无参构造方法
    public final Constructor constructor;

    //解析的字段信息集合
    public FieldInfo[] fieldInfos;

    /**
     * 类中{@link PacketField#var()}属性存在的字段个数
     */
    public int varValArrLen=0;

    /**
     * 类中{@link PacketField#var()}属性存在的字段中最小char(以char对应int来排序)对应的int
     */
    public int varValArrOffset=0;


    public PacketInfo(Class clazz) {
        this.clazz = clazz;
        try {
            this.constructor = clazz.getConstructor();
            this.constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw BaseRuntimeException.getException(e);
        }
    }
}

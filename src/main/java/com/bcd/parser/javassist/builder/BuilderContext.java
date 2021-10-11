package com.bcd.parser.javassist.builder;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.javassist.Parser;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import io.netty.buffer.ByteBuf;
import javassist.CtClass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于构造字段asm代码用到的环境变量
 */
public class BuilderContext {
    /**
     * 所属parser
     */
    public final Parser parser;
    /**
     * parse方法体
     */
    public final StringBuilder body;
    /**
     * 生产的{@link com.bcd.parser.javassist.JavassistParser}子类
     */
    public final CtClass implCc;
    /**
     * 字段所属类实例变量名称
     */
    public final String instance_var_name;
    /**
     * 父构造环境
     */
    public final BuilderContext parentContext;

    /**
     * 当前字段所属class中的变量名称对应字段名称
     */
    public final Map<Character, String> var_to_fieldName = new HashMap<>();



    /**
     * 当前字段
     */
    public Field field;
    /**
     * 当前字段{@link PacketField}注解
     */
    public PacketField packetField;
    /**
     * 长度变量
     */
    public String lenRes;

    /**
     * 用于给
     * {@link com.bcd.parser.javassist.processor.FieldProcessor#process(ByteBuf, FieldProcessContext)}
     * 的参数对象、对象复用、避免构造多个
     */
    public String processorBuildContextVarName;

    public BuilderContext(StringBuilder body, Parser parser, CtClass implCc, String instance_var_name, BuilderContext parentContext) {
        this.body = body;
        this.parser = parser;
        this.implCc = implCc;
        this.instance_var_name = instance_var_name;
        this.parentContext = parentContext;
    }
}

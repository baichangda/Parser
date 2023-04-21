package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.*;
import com.bcd.support_parser.processor.Processor;
import com.bcd.support_parser.processor.ProcessContext;
import com.bcd.support_parser.util.JavassistUtil;
import io.netty.buffer.ByteBuf;
import javassist.CtClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BuilderContext {
    /**
     * 主要用于{@link F_customize#builderClass()}缓存、避免在生成解析类的过程中生成多个实例
     */
    public final static Map<Class, FieldBuilder> fieldBuilderCache = new HashMap<>();
    /**
     * parse方法体
     */
    public final StringBuilder body;
    /**
     * 类
     */
    public final Class clazz;
    /**
     * 生产的{@link Processor}子类
     */
    public final CtClass implCc;
    /**
     * 父构造环境
     */
    public final BuilderContext parentContext;
    /**
     * 当前字段
     */
    public Field field;

    /**
     * 主要用于处理
     * {@link com.bcd.support_parser.anno.F_integer}
     * {@link F_float_integer}
     * 使用bit时候
     */
    public Map<String, int[]> fieldNameToBitInfo = new HashMap<>();
    public String varNameBitBytes;

    /**
     * 用于给
     * {@link com.bcd.support_parser.processor.Processor#process(ByteBuf, ProcessContext)}
     * {@link com.bcd.support_parser.processor.Processor#deProcess(ByteBuf, ProcessContext, Object)}
     * 的参数对象、对象复用、避免构造多个
     */
    public String processContextVarName;

    /**
     * 当前字段所属class中的变量名称对应字段名称
     */
    public final Map<Character, String> varToFieldName = new HashMap<>();

    /**
     * 类全局变量定义内容对应变量名称
     * 避免重复定义类变量
     */
    public final Map<String, String> classVarDefineToVarName;

    /**
     * 用于{@link F_skip#mode()} 为 {@link com.bcd.support_parser.anno.SkipMode#ReservedFromPrevReserved} 时候
     * <p>
     * 此变量名称代表相同bean内上一个字段
     * {@link F_skip#mode()}为
     * {@link com.bcd.support_parser.anno.SkipMode#ReservedFromStart} 或
     * {@link com.bcd.support_parser.anno.SkipMode#ReservedFromPrevReserved}
     * 时候索引的位置变量名称
     * 如果没有上一个这样的字段、则取值是{@link FieldBuilder#startIndexVarName}
     */
    public String prevSkipReservedIndexVarName = FieldBuilder.startIndexVarName;
    public final Set<String> indexFieldNameSet = new HashSet<>();

    /**
     * {@link T_order#order()}的字节序模式
     * 如果本类没有使用{@link T_order}注解、则从父类继承
     */
    public final ByteOrder order;

    private void initIndexFieldNameSet() {
        String prevSkipReservedFieldName = null;
        for (Field declaredField : clazz.getDeclaredFields()) {
            final F_skip f_skip = declaredField.getAnnotation(F_skip.class);
            if (f_skip != null) {
                switch (f_skip.mode()) {
                    case ReservedFromStart -> {
                        prevSkipReservedFieldName = declaredField.getName();
                    }
                    case ReservedFromPrevReserved -> {
                        if (prevSkipReservedFieldName != null) {
                            indexFieldNameSet.add(prevSkipReservedFieldName);
                        }
                        prevSkipReservedFieldName = declaredField.getName();
                    }
                }
            }
        }
    }

    private ByteOrder initOrder() {
        final T_order t_order = (T_order) clazz.getAnnotation(T_order.class);
        if (t_order == null) {
            return parentContext == null ? null : parentContext.order;
        } else {
            return t_order.order();
        }
    }

    public BuilderContext(StringBuilder body, Class clazz, CtClass implCc, BuilderContext parentContext, Map<String, String> classVarDefineToVarName) {
        this.body = body;
        this.clazz = clazz;
        this.implCc = implCc;
        this.parentContext = parentContext;
        this.classVarDefineToVarName = classVarDefineToVarName;
        initIndexFieldNameSet();
        order = initOrder();
    }

    public final String getProcessContextVarName() {
        if (processContextVarName == null) {
            processContextVarName = "processContext";
            final String proocessContextClassName = ProcessContext.class.getName();
            JavassistUtil.append(body, "final {} {}=new {}({},{});\n",
                    proocessContextClassName,
                    processContextVarName,
                    proocessContextClassName,
                    FieldBuilder.varNameInstance,
                    FieldBuilder.varNameParentProcessContext);
        }
        return processContextVarName;
    }
}

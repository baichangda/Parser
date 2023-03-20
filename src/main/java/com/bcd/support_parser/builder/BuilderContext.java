package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_float_integer;
import com.bcd.support_parser.anno.F_userDefine;
import com.bcd.support_parser.processor.Processor;
import com.bcd.support_parser.processor.ProcessContext;
import com.bcd.support_parser.util.JavassistUtil;
import io.netty.buffer.ByteBuf;
import javassist.CtClass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BuilderContext {

    /**
     * 主要用于{@link F_userDefine#builderClass()}缓存、避免在生成解析类的过程中生成多个实例
     */
    public final static Map<Class, FieldBuilder> fieldBuilderCache = new HashMap<>();
    /**
     * parse方法体
     */
    public final StringBuilder body;
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
    public final Map<String,String> classVarDefineToVarName;

    public BuilderContext(StringBuilder body, CtClass implCc, BuilderContext parentContext,Map<String,String> classVarDefineToVarName) {
        this.body = body;
        this.implCc = implCc;
        this.parentContext = parentContext;
        this.classVarDefineToVarName=classVarDefineToVarName;
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

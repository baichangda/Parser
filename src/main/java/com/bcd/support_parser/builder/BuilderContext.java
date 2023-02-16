package com.bcd.support_parser.builder;

import com.bcd.support_parser.anno.F_userDefine;
import com.bcd.support_parser.Parser;
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
     * 所属parser
     */
    public final Parser parser;
    /**
     * parse方法体
     */
    public final StringBuilder body;
    /**
     * 实例对象名称
     */
    public final String varNameInstance;
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
     * {@link com.bcd.support_parser.anno.F_float}
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

    public BuilderContext(StringBuilder body, Parser parser, CtClass implCc, String varNameInstance, BuilderContext parentContext) {
        this.body = body;
        this.parser = parser;
        this.implCc = implCc;
        this.varNameInstance = varNameInstance;
        this.parentContext = parentContext;
    }

    public final String getProcessContextVarName() {
        if (processContextVarName == null) {
            processContextVarName = JavassistUtil.getVarName(this, "processContext");
            final String fieldContextCLassName = ProcessContext.class.getName();
            JavassistUtil.append(body, "final {} {}=new {}({},{});\n",
                    fieldContextCLassName,
                    processContextVarName,
                    fieldContextCLassName,
                    FieldBuilder.varNameInstance,
                    FieldBuilder.varNameParentProcessContext);
        }
        return processContextVarName;
    }
}

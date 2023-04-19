package com.bcd.support_parser;

import com.bcd.support_parser.anno.*;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.builder.*;
import com.bcd.support_parser.processor.Processor;
import com.bcd.support_parser.processor.ProcessContext;
import com.bcd.support_parser.util.JavassistUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import javassist.*;
import javassist.bytecode.SignatureAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 解析器
 * 配合注解完成解析工作
 * 会忽视如下字段
 * 1、没有被{@link #annoSet}中注解标注的字段
 * 2、static或者final修饰的字段
 * 3、非public字段
 * <p>
 * 工作原理:
 * 使用javassist框架配合自定义注解、生成一套解析代码
 * <p>
 * 调用入口:
 * {@link #parse(Class, ByteBuf, ProcessContext)}
 * <p>
 * 性能表现:
 * 由于是字节码增强技术、和手动编写代码解析效率一样
 */
public class Parser {

    public final static Logger logger = LoggerFactory.getLogger(Parser.class);

    public final static FieldBuilder__F_bean fieldBuilder__f_bean = new FieldBuilder__F_bean();
    public final static FieldBuilder__F_bean_list fieldBuilder__f_bean_list = new FieldBuilder__F_bean_list();
    public final static FieldBuilder__F_date fieldBuilder__f_date = new FieldBuilder__F_date();
    public final static FieldBuilder__F_float_integer_array fieldBuilder__f_float_integer_array = new FieldBuilder__F_float_integer_array();
    public final static FieldBuilder__F_float_integer fieldBuilder__f_float_integer = new FieldBuilder__F_float_integer();
    public final static FieldBuilder__F_float_ieee754_array fieldBuilder__F_float_ieee754_array = new FieldBuilder__F_float_ieee754_array();
    public final static FieldBuilder__F_float_ieee754 fieldbuilder__f_float_ieee754 = new FieldBuilder__F_float_ieee754();
    public final static FieldBuilder__F_integer_array fieldBuilder__f_integer_array = new FieldBuilder__F_integer_array();
    public final static FieldBuilder__F_integer fieldBuilder__f_integer_ = new FieldBuilder__F_integer();
    public final static FieldBuilder__F_skip fieldBuilder__f_skip = new FieldBuilder__F_skip();
    public final static FieldBuilder__F_string fieldBuilder__f_string = new FieldBuilder__F_string();
    public final static FieldBuilder__F_customize fieldBuilder__f_customize = new FieldBuilder__F_customize();


    public final static Set<Class> annoSet = new HashSet<>();

    static {
        annoSet.add(F_integer.class);
        annoSet.add(F_integer_array.class);

        annoSet.add(F_float_integer.class);
        annoSet.add(F_float_integer_array.class);

        annoSet.add(F_float_ieee754.class);
        annoSet.add(F_float_ieee754_array.class);

        annoSet.add(F_string.class);

        annoSet.add(F_date.class);

        annoSet.add(F_bean.class);
        annoSet.add(F_bean_list.class);

        annoSet.add(F_customize.class);

        annoSet.add(F_skip.class);
    }


    private final static Map<Class, Processor> beanClass_to_processor = new HashMap<>();

    /**
     * 是否在src/main/java下面生成class文件
     * 主要用于开发测试阶段、便于查看生成的结果
     */
    public static boolean generateClassFile = false;

    /**
     * 是否打印javassist生成class的过程日志
     */
    public static boolean printBuildLog = false;

    public interface LogCollector_parse {
        /**
         * 收集每个字段解析的详情
         *
         * @param fieldClass         字段类型
         * @param fieldName          字段名称
         * @param content            解析之前字节数组
         * @param val                解析后的值
         * @param processorClassName 解析器类名
         */
        void collect_field(Class fieldClass, String fieldName, byte[] content, Object val, String processorClassName);

    }

    public interface LogCollector_deParse {
        /**
         * 收集每个字段解析的详情
         *
         * @param fieldClass         字段类型
         * @param fieldName          字段名称
         * @param content            对象属性值
         * @param val                解析后的字节数组
         * @param processorClassName 解析器类名
         */
        void collect_field(Class fieldClass, String fieldName, Object val, byte[] content, String processorClassName);

    }

    /**
     * 解析log采集器
     * 需要注意的是、此功能用于调试、会在生成的class中加入日志代码、影响性能
     * 而且此功能开启时候避免多线程调用解析、会产生日志混淆、不易调试
     */
    public static LogCollector_parse logCollector_parse;
    public static LogCollector_deParse logCollector_deParse;


    public static void withDefaultLogCollector_parse() {
        logCollector_parse = new LogCollector_parse() {
            @Override
            public void collect_field(Class fieldClass, String fieldName, byte[] content, Object val, String processorClassName) {
                logger.info("--parse field--[{}].[{}] [{}] [{}]->[{}]"
                        , fieldClass.getSimpleName()
                        , fieldName
                        , processorClassName
                        , ByteBufUtil.hexDump(content)
                        , val
                );
            }

        };
    }

    public static void withDefaultLogCollector_deParse() {
        logCollector_deParse = new LogCollector_deParse() {
            @Override
            public void collect_field(Class fieldClass, String fieldName, Object val, byte[] content, String processorClassName) {
                logger.info("--deParse field--[{}].[{}] [{}] [{}]->[{}]"
                        , fieldClass.getSimpleName()
                        , fieldName
                        , processorClassName
                        , val
                        , ByteBufUtil.hexDump(content)
                );
            }

        };
    }

    public static void enablePrintBuildLog() {
        printBuildLog = true;
    }

    public static void enableGenerateClassFile() {
        generateClassFile = true;
    }

    /**
     * 计算bit字段属性、解析时候使用
     * map
     * key为字段名称
     * val [bit偏移、当前字段是否是bit组的最后一个字段、当前组bit计算的byte数组长度]
     *
     * @param fieldList
     * @param builderContext
     * @return
     */
    private static Map<String, int[]> calcBitField(List<Field> fieldList, BuilderContext builderContext) {
        Map<String, int[]> fieldNameToBitInfo = new HashMap<>();
        List<int[]> tempList = new ArrayList<>();
        int bitSum = 0;
        for (int i = 0; i < fieldList.size(); i++) {
            final Field field = fieldList.get(i);
            final F_integer f_integer = field.getAnnotation(F_integer.class);
            if (f_integer != null && f_integer.bit() > 0) {
                final int[] ints = new int[]{bitSum, 0, 0};
                tempList.add(ints);
                fieldNameToBitInfo.put(field.getName(), ints);
                bitSum += f_integer.bit();
                continue;
            }
            final F_float_integer f_floatInteger = field.getAnnotation(F_float_integer.class);
            if (f_floatInteger != null && f_floatInteger.bit() > 0) {
                final int[] ints = new int[]{bitSum, 0, 0};
                tempList.add(ints);
                fieldNameToBitInfo.put(field.getName(), ints);
                bitSum += f_floatInteger.len();
                continue;
            }
            if (!tempList.isEmpty()) {
                final int byteLen = bitSum / 8 + (bitSum % 8 == 0 ? 0 : 1);
                tempList.get(tempList.size() - 1)[1] = 1;
                for (int[] ints : tempList) {
                    ints[2] = byteLen;
                }
                tempList.clear();
                bitSum = 0;
            }
        }
        return fieldNameToBitInfo;
    }

    private static boolean needParse(Field field) {
        final Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annoSet.contains(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public static void buildMethodBody_parse(Class clazz, BuilderContext context) {
        //过滤掉 final、static关键字修饰、且不是public的字段
        final List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(e ->
                        needParse(e) &&
                                !Modifier.isFinal(e.getModifiers()) &&
                                !Modifier.isStatic(e.getModifiers()) &&
                                Modifier.isPublic(e.getModifiers()))
                .collect(Collectors.toList());
        if (fieldList.isEmpty()) {
            return;
        }
        //需要提前计算出F_integer_bit、F_float_bit的占用字节数、以及各个字段的在其中的偏移量
        context.fieldNameToBitInfo = calcBitField(fieldList, context);

        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            if (logCollector_parse != null) {
                JavassistUtil.prependLogCode_parse(context);
            }
            try {
                final F_integer f_integer = field.getAnnotation(F_integer.class);
                if (f_integer != null) {
                    fieldBuilder__f_integer_.buildParse(context);
                    continue;
                }

                final F_float_integer f_floatInteger = field.getAnnotation(F_float_integer.class);
                if (f_floatInteger != null) {
                    fieldBuilder__f_float_integer.buildParse(context);
                    continue;
                }

                final F_float_ieee754 f_float_ieee754 = field.getAnnotation(F_float_ieee754.class);
                if (f_float_ieee754 != null) {
                    fieldbuilder__f_float_ieee754.buildParse(context);
                    continue;
                }

                final F_integer_array f_integer_array = field.getAnnotation(F_integer_array.class);
                if (f_integer_array != null) {
                    fieldBuilder__f_integer_array.buildParse(context);
                    continue;
                }

                final F_float_integer_array f_float_integer_array = field.getAnnotation(F_float_integer_array.class);
                if (f_float_integer_array != null) {
                    fieldBuilder__f_float_integer_array.buildParse(context);
                    continue;
                }

                final F_float_ieee754_array f_float_ieee754_array = field.getAnnotation(F_float_ieee754_array.class);
                if (f_float_ieee754_array != null) {
                    fieldBuilder__F_float_ieee754_array.buildParse(context);
                    continue;
                }

                final F_string f_string = field.getAnnotation(F_string.class);
                if (f_string != null) {
                    fieldBuilder__f_string.buildParse(context);
                    continue;
                }

                final F_date f_date = field.getAnnotation(F_date.class);
                if (f_date != null) {
                    fieldBuilder__f_date.buildParse(context);
                    continue;
                }

                final F_bean f_bean = field.getAnnotation(F_bean.class);
                if (f_bean != null) {
                    fieldBuilder__f_bean.buildParse(context);
                    continue;
                }

                final F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
                if (f_bean_list != null) {
                    fieldBuilder__f_bean_list.buildParse(context);
                    continue;
                }

                final F_customize f_customize = field.getAnnotation(F_customize.class);
                if (f_customize != null) {
                    fieldBuilder__f_customize.buildParse(context);
                }

                final F_skip f_skip = field.getAnnotation(F_skip.class);
                if (f_skip != null) {
                    fieldBuilder__f_skip.buildParse(context);
                    continue;
                }
            } finally {
                if (logCollector_parse != null) {
                    JavassistUtil.appendLogCode_parse(context);
                }
            }
        }

    }

    public static void buildMethodBody_deParse(Class clazz, BuilderContext context) {
        //过滤掉 final、static关键字修饰、且不是public的字段
        final List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(e ->
                        needParse(e) &&
                                !Modifier.isFinal(e.getModifiers()) &&
                                !Modifier.isStatic(e.getModifiers()) &&
                                Modifier.isPublic(e.getModifiers()))
                .collect(Collectors.toList());
        //需要提前计算出F_integer_bit、F_float_bit的占用字节数、以及各个字段的在其中的偏移量
        context.fieldNameToBitInfo = calcBitField(fieldList, context);
        if (fieldList.isEmpty()) {
            return;
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            try {
                if (logCollector_deParse != null) {
                    JavassistUtil.prependLogCode_deParse(context);
                }
                final F_integer f_integer = field.getAnnotation(F_integer.class);
                if (f_integer != null) {
                    fieldBuilder__f_integer_.buildDeParse(context);
                    continue;
                }

                final F_float_integer f_floatInteger = field.getAnnotation(F_float_integer.class);
                if (f_floatInteger != null) {
                    fieldBuilder__f_float_integer.buildDeParse(context);
                    continue;
                }

                final F_float_ieee754 f_float_ieee754 = field.getAnnotation(F_float_ieee754.class);
                if (f_float_ieee754 != null) {
                    fieldbuilder__f_float_ieee754.buildDeParse(context);
                    continue;
                }

                final F_integer_array f_integer_array = field.getAnnotation(F_integer_array.class);
                if (f_integer_array != null) {
                    fieldBuilder__f_integer_array.buildDeParse(context);
                    continue;
                }

                final F_float_integer_array f_float_integer_array = field.getAnnotation(F_float_integer_array.class);
                if (f_float_integer_array != null) {
                    fieldBuilder__f_float_integer_array.buildDeParse(context);
                    continue;
                }

                final F_float_ieee754_array f_float_ieee754_array = field.getAnnotation(F_float_ieee754_array.class);
                if (f_float_ieee754_array != null) {
                    fieldBuilder__F_float_ieee754_array.buildDeParse(context);
                    continue;
                }

                final F_string f_string = field.getAnnotation(F_string.class);
                if (f_string != null) {
                    fieldBuilder__f_string.buildDeParse(context);
                    continue;
                }

                final F_date f_date = field.getAnnotation(F_date.class);
                if (f_date != null) {
                    fieldBuilder__f_date.buildDeParse(context);
                    continue;
                }

                final F_bean f_bean = field.getAnnotation(F_bean.class);
                if (f_bean != null) {
                    fieldBuilder__f_bean.buildDeParse(context);
                    continue;
                }

                final F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
                if (f_bean_list != null) {
                    fieldBuilder__f_bean_list.buildDeParse(context);
                    continue;
                }

                final F_customize f_customize = field.getAnnotation(F_customize.class);
                if (f_customize != null) {
                    fieldBuilder__f_customize.buildDeParse(context);
                }

                final F_skip f_skip = field.getAnnotation(F_skip.class);
                if (f_skip != null) {
                    fieldBuilder__f_skip.buildDeParse(context);
                    continue;
                }
            } finally {
                if (logCollector_deParse != null) {
                    JavassistUtil.appendLogCode_deParse(context);
                }
            }
        }
    }

    static int processorIndex = 0;

    public static Class buildClass(Class clazz) throws CannotCompileException, NotFoundException, IOException {
        final String processor_class_name = Processor.class.getName();
        final String byteBufClassName = ByteBuf.class.getName();
        final String clazzName = clazz.getName();

        final int lastIndexOf = processor_class_name.lastIndexOf(".");
        String implProcessor_class_name = processor_class_name.substring(0, lastIndexOf) + "." + processor_class_name.substring(lastIndexOf + 1) + "_" + processorIndex++ + "_" + clazz.getSimpleName();
        final CtClass cc = ClassPool.getDefault().makeClass(implProcessor_class_name);

        //添加泛型
        SignatureAttribute.ClassSignature class_cs = new SignatureAttribute.ClassSignature(null, null, new SignatureAttribute.ClassType[]{
                new SignatureAttribute.ClassType(processor_class_name, new SignatureAttribute.TypeArgument[]{
                        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(clazzName))
                })
        });
        cc.setGenericSignature(class_cs.encode());

        cc.setModifiers(Modifier.FINAL | Modifier.PUBLIC);

        StringBuilder initBody = new StringBuilder();
        final CtConstructor constructor = CtNewConstructor.make(new CtClass[]{}, null, cc);
        initBody.append("{\n");
        //加processorClass字段并初始化
        final List<Class> processorClassList = Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getAnnotation(F_customize.class)).filter(Objects::nonNull).map(F_customize::processorClass).filter(e -> e != void.class).collect(Collectors.toList());
        for (Class processorClass : processorClassList) {
            final String processorClassName = processorClass.getName();
            final String processorVarName = JavassistUtil.getProcessorVarName(processorClass);
            cc.addField(CtField.make("private final " + processorClassName + " " + processorVarName + ";", cc));
            initBody.append(JavassistUtil.format("this.{}=new {}();\n", processorVarName, processorClassName));
        }
        initBody.append("}\n");
        if (printBuildLog) {
            logger.info("----------clazz[{}] constructor body-------------\n{}", clazz.getName(), initBody.toString());
        }
        constructor.setBody(initBody.toString());
        cc.addConstructor(constructor);

        final Map<String, String> classVarDefineToVarName = new HashMap<>();

        //添加实现、定义process方法
        final CtClass interface_cc = ClassPool.getDefault().get(processor_class_name);
        cc.addInterface(interface_cc);
        final CtMethod process_cm = CtNewMethod.make(
                /**
                 * 在这里定义返回值为Object类型
                 * 因为正常的继承、asm实现方法需要额外创建一个桥接方法、针对泛型部分的参数为Object类型
                 */
                ClassPool.getDefault().get(Object.class.getName()),
                "process",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(ProcessContext.class.getName())
                }, null, null, cc);

        cc.addMethod(process_cm);
        //process方法体
        StringBuilder processBody = new StringBuilder();
        processBody.append("\n{\n");
        JavassistUtil.append(processBody, "final {} {}=new {}();\n", clazzName, FieldBuilder.varNameInstance, clazzName);
        boolean hasFieldSkipModeReserved = Arrays.stream(clazz.getDeclaredFields()).anyMatch(e -> {
            final F_skip f_skip = e.getAnnotation(F_skip.class);
            if (f_skip == null) {
                return false;
            } else {
                return f_skip.mode() == SkipMode.ReservedFromStart || f_skip.mode() == SkipMode.ReservedFromPrevReserved;
            }
        });
        if (hasFieldSkipModeReserved) {
            JavassistUtil.append(processBody, "final int {}={}.readerIndex();\n", FieldBuilder.startIndexVarName, FieldBuilder.varNameByteBuf);
        }
        BuilderContext parseContext = new BuilderContext(processBody, clazz, cc, null, classVarDefineToVarName);
        buildMethodBody_parse(clazz, parseContext);
        JavassistUtil.append(processBody, "return {};\n", FieldBuilder.varNameInstance);
        processBody.append("}");
        if (printBuildLog) {
            logger.info("\n-----------class[{}] process-----------{}\n", clazz.getName(), processBody.toString());
        }
        process_cm.setBody(processBody.toString());

        //添加实现、定义deProcess方法
        final CtMethod deProcess_cm = CtNewMethod.make(
                ClassPool.getDefault().get(void.class.getName()),
                "deProcess",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(ProcessContext.class.getName()),
                        ClassPool.getDefault().get(Object.class.getName()),
                }, null, null, cc);

        cc.addMethod(deProcess_cm);
        //deProcess方法体
        StringBuilder deProcessBody = new StringBuilder();
        deProcessBody.append("\n{\n");
        JavassistUtil.append(deProcessBody, "final {} {}=({})$3;\n", clazzName, FieldBuilder.varNameInstance, clazzName);
        if (hasFieldSkipModeReserved) {
            JavassistUtil.append(deProcessBody, "final int {}={}.writerIndex();\n", FieldBuilder.startIndexVarName, FieldBuilder.varNameByteBuf);
        }
        BuilderContext deParseContext = new BuilderContext(deProcessBody, clazz, cc, null, classVarDefineToVarName);
        buildMethodBody_deParse(clazz, deParseContext);
        deProcessBody.append("}");
        if (printBuildLog) {
            logger.info("\n-----------class[{}] deProcess-----------{}\n", clazz.getName(), deProcessBody.toString());
        }
        deProcess_cm.setBody(deProcessBody.toString());

        if (generateClassFile) {
            cc.writeFile("src/main/java");
        }
        return cc.toClass(Processor.class);
//        return cc.toClass();
    }


    public static <T> T parse(Class<T> clazz, ByteBuf data, ProcessContext parentContext) {
        Processor<T> processor = beanClass_to_processor.get(clazz);
        if (processor == null) {
            synchronized (beanClass_to_processor) {
                processor = beanClass_to_processor.get(clazz);
                if (processor == null) {
                    try {
                        final Class impl = buildClass(clazz);
                        processor = (Processor<T>) (impl.getConstructor().newInstance());
                        beanClass_to_processor.put(clazz, processor);
                    } catch (Exception e) {
                        throw BaseRuntimeException.getException(e);
                    }
                }
            }
        }
        return processor.process(data, parentContext);
    }

    public static void deParse(Object instance, ByteBuf data, ProcessContext parentContext) {
        final Class<?> clazz = instance.getClass();
        Processor processor = beanClass_to_processor.get(clazz);
        if (processor == null) {
            synchronized (beanClass_to_processor) {
                processor = beanClass_to_processor.get(clazz);
                if (processor == null) {
                    try {
                        final Class impl = buildClass(clazz);
                        processor = (Processor) (impl.getConstructor().newInstance());
                        beanClass_to_processor.put(clazz, processor);
                    } catch (Exception e) {
                        throw BaseRuntimeException.getException(e);
                    }
                }
            }
        }
        processor.deProcess(data, parentContext, instance);
    }
}

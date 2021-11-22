package com.bcd.parser.javassist;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.javassist.builder.*;
import com.bcd.parser.javassist.parser.JavassistParser;
import com.bcd.parser.javassist.processor.FieldProcessContext;
import com.bcd.parser.javassist.util.JavassistUtil;
import io.netty.buffer.ByteBuf;
import javassist.*;
import javassist.bytecode.SignatureAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 协议解析器
 * 主要功能如下:
 * 1、解析
 * 从{@link ByteBuf}中获取二进制数据、解析成class对象实例
 * 解析规则参照{@link com.bcd.parser.anno.PacketField}
 * <p>
 * 原理简介
 * 通过使用第三方工具包Javassist
 * 根据{@link PacketField}动态生成{@link JavassistParser}的子类、生成子类对象、调用对象{@link JavassistParser#parse(ByteBuf, FieldProcessContext)}完成解析
 * <p>
 * 主要类介绍
 * {@link FieldBuilder}
 * 各种类型字段的字节码构造器、提供的类型在{@link Parser}中的变量可以看到
 * 如果当前某个类型规则和实现不一致、可以替换{@link Parser}中对应的变量、注意需要在{@link Parser#init()}前替换
 * <p>
 * {@link com.bcd.parser.javassist.processor.FieldProcessor}
 * 用于支持{@link PacketField#processorClass()}完成自定义解析
 * 注意所有的子类必须提供参数为空的构造方法
 * <p>
 * {@link BuilderContext}
 * 执行{@link FieldBuilder#build(BuilderContext)}的上下文环境
 * <p>
 * {@link FieldProcessContext}
 * 执行{@link com.bcd.parser.javassist.processor.FieldProcessor#process(ByteBuf, FieldProcessContext)}的上下文环境
 * <p>
 * 性能表现:
 * 以gb32960协议为例子
 * cpu: Intel(R) Core(TM) i5-7360U CPU @ 2.30GHz
 * 单线程、在cpu使用率90%+ 的情况下
 * 解析速度约为 120-125w/s、多个线程成倍数增长
 * 具体查看{@link com.bcd.parser.impl.gb32960.javassist.Parser_gb32960#main(String[])}
 * 注意:
 * 因为是cpu密集型运算、所以性能达到计算机物理核心个数后已经达到上限、不能以逻辑核心为准、此时虽然整体cpu使用率没有满、但这只是top使用率显示问题
 * 例如 2核4线程 、物理核心2个、逻辑核心4个、此时使用2个线程就能用尽cpu资源、即使指标显示cpu使用率50%、其实再加线程已经没有提升
 * <p>
 * 遗留问题:
 * 1、如果当一个字段需要作为变量供其他表达式使用、且此时变量解析出来的值为无效或者异常、会导致解析出错;
 * 要解决这个问题需要设置字段自定义处理器{@link PacketField#processorClass()}
 * <p>
 * 注意事项:
 * 1、不支持基础类型的包装类型、因为在字节码层面需要进行boxing、unBoxing处理、过于复杂
 * 2、在使用时候需要定义字段类型 能完全 容纳下协议文档中所占用字节数
 * (包括容纳下异常值、无效值,这两种值一般是0xfe、0xff结尾; 例如两个字节即为0xfffe、0xffff、此时需要用int存储才能正确表示其值)
 */
public class Parser {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ShortFieldBuilder shortFieldBuilder = new ShortFieldBuilder();
    public ByteFieldBuilder byteFieldBuilder = new ByteFieldBuilder();
    public IntFieldBuilder intFieldBuilder = new IntFieldBuilder();
    public LongFieldBuilder longFieldBuilder = new LongFieldBuilder();
    public ByteArrayFieldBuilder byteArrayFieldBuilder = new ByteArrayFieldBuilder();
    public ShortArrayFieldBuilder shortArrayFieldBuilder = new ShortArrayFieldBuilder();
    public IntArrayFieldBuilder intArrayFieldBuilder = new IntArrayFieldBuilder();
    public LongArrayFieldBuilder longArrayFieldBuilder = new LongArrayFieldBuilder();
    public FloatFieldBuilder floatFieldBuilder = new FloatFieldBuilder();
    public DoubleFieldBuilder doubleFieldBuilder = new DoubleFieldBuilder();
    public StringFieldBuilder stringFieldBuilder = new StringFieldBuilder();
    public ProcessorClassFieldBuilder processorClassFieldBuilder = new ProcessorClassFieldBuilder();
    public FloatArrayFieldBuilder floatArrayFieldBuilder = new FloatArrayFieldBuilder();
    public DoubleArrayFieldBuilder doubleArrayFieldBuilder = new DoubleArrayFieldBuilder();
    public ParseableObjectFieldBuilder parseableObjectFieldBuilder = new ParseableObjectFieldBuilder();
    public DateFieldBuilder dateFieldBuilder = new DateFieldBuilder();
    public ParseableObjectListFieldBuilder parseableObjectListFieldBuilder = new ParseableObjectListFieldBuilder();
    public ParseableObjectArrayFieldBuilder parseableObjectArrayFieldBuilder = new ParseableObjectArrayFieldBuilder();

    public final Map<Class, JavassistParser> beanClass_to_javassistParser = new HashMap<>();

    /**
     * 当处理字段类型为(Bean 或 Bean[] 或 List<Bean>)时候、其中Bean为自定义对象
     * 这些Bean将不生成新的{@link JavassistParser}子类来解析
     * 而是将解析的逻辑直接集成在字段所属实体类的class的解析方法中
     */
    public boolean allInOne = false;

    /**
     * 是否在src/main/java下面生成class文件
     * 主要用于开发测试阶段、便于查看生成的结果
     */
    public boolean generateClassField = true;

    public void init() {
        initFieldBuilder();
    }

    /**
     * 初始化所有的{@link FieldBuilder}实现
     */
    public void initFieldBuilder() {
        try {
            final Field[] fields = this.getClass().getFields();
            for (Field declaredField : fields) {
                if (FieldBuilder.class.isAssignableFrom(declaredField.getType())) {
                    declaredField.setAccessible(true);
                    final FieldBuilder fieldBuilder;
                    fieldBuilder = (FieldBuilder) declaredField.get(this);
                    fieldBuilder.parser = this;
                }
            }
        } catch (IllegalAccessException e) {
            throw BaseRuntimeException.getException(e);
        }
    }

    public void buildParseMethodBody(Class clazz, BuilderContext context) {
        final Field[] declaredFields = clazz.getDeclaredFields();
        final List<Object[]> collect = Arrays.stream(declaredFields).map(f -> new Object[]{f, f.getAnnotation(PacketField.class)}).filter(e -> e[1] != null)
                .sorted(Comparator.comparing(e -> ((PacketField) e[1]).index())).collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            Field field = (Field) collect.get(i)[0];
            PacketField packetField = (PacketField) collect.get(i)[1];


            /**
             * 解析字段占用字节长度
             */
            String lenRes;
            if (packetField.len() == 0) {
                if (packetField.lenExpr().isEmpty()) {
                    lenRes = null;
                } else {
                    lenRes = JavassistUtil.replaceVarToFieldName(packetField.lenExpr(), context.varToFieldName, field);
                }
            } else {
                lenRes = packetField.len() + "";
            }

            /**
             * 处理 {@link PacketField#skipParse()}
             */
            if (packetField.skipParse()) {
                if (lenRes == null) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] @PacketField[skipParse=true] not support", clazz.getName(), field.getName(), packetField.singleLen());
                } else {
                    JavassistUtil.append(context.body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, lenRes);
                    continue;
                }
            }

            final Class builderClass = packetField.builderClass();
            final Class processorClass = packetField.processorClass();
            context.field = field;
            context.packetField = packetField;
            context.lenRes = lenRes;
            if (builderClass != void.class) {
                try {
                    final FieldBuilder builder = (FieldBuilder) builderClass.newInstance();
                    builder.build(context);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw BaseRuntimeException.getException(e);
                }
            } else if (processorClass != void.class) {
                processorClassFieldBuilder.build(context);
            } else {
                final Class<?> type = field.getType();
                if (type.isAssignableFrom(byte[].class)) {
                    byteArrayFieldBuilder.build(context);
                } else if (type.isAssignableFrom(short.class)) {
                    shortFieldBuilder.build(context);
                } else if (type.isAssignableFrom(int.class)) {
                    intFieldBuilder.build(context);
                } else if (type.isAssignableFrom(byte.class)) {
                    byteFieldBuilder.build(context);
                } else if (type.isAssignableFrom(long.class)) {
                    longFieldBuilder.build(context);
                } else if (type.isAssignableFrom(String.class)) {
                    stringFieldBuilder.build(context);
                } else if (type.isAssignableFrom(long[].class)) {
                    longArrayFieldBuilder.build(context);
                } else if (type.isAssignableFrom(int[].class)) {
                    intArrayFieldBuilder.build(context);
                } else if (type.isAssignableFrom(short[].class)) {
                    shortArrayFieldBuilder.build(context);
                } else if (type.isAssignableFrom(float.class)) {
                    floatFieldBuilder.build(context);
                } else if (type.isAssignableFrom(double.class)) {
                    doubleFieldBuilder.build(context);
                } else if (type.isAssignableFrom(float[].class)) {
                    floatArrayFieldBuilder.build(context);
                } else if (type.isAssignableFrom(double[].class)) {
                    doubleArrayFieldBuilder.build(context);
                } else if (type.isAssignableFrom(Date.class)) {
                    dateFieldBuilder.build(context);
                } else if (type.isAssignableFrom(List.class)) {
                    parseableObjectListFieldBuilder.build(context);
                } else if (type.isArray()) {
                    parseableObjectArrayFieldBuilder.build(context);
                } else {
                    parseableObjectFieldBuilder.build(context);
                }
            }
        }
    }

    public void buildAppend(StringBuilder body, Class clazz, String fieldVarName, Parser parser, BuilderContext fieldBuilderContext) {
        BuilderContext builderContext = new BuilderContext(body, parser, fieldBuilderContext.implCc, fieldVarName, fieldBuilderContext);
        buildParseMethodBody(clazz, builderContext);
    }

    public void buildAppend(BuilderContext fieldBuilderContext) {
        final Class clazz = fieldBuilderContext.field.getType().getClass();
        String fieldVarName = JavassistUtil.getFieldVarName(fieldBuilderContext);
        BuilderContext builderContext = new BuilderContext(fieldBuilderContext.body, fieldBuilderContext.parser, fieldBuilderContext.implCc, fieldVarName, fieldBuilderContext);
        buildParseMethodBody(clazz, builderContext);
    }

    public Class buildClass(Class clazz) throws CannotCompileException, NotFoundException, IOException {
        final String javassistParser_class_name = JavassistParser.class.getName();
        final String byteBufClassName = ByteBuf.class.getName();
        final String clazzName = clazz.getName();

        final int lastIndexOf = javassistParser_class_name.lastIndexOf(".");
        String implJavassistParser_class_name = javassistParser_class_name.substring(0, lastIndexOf) + "." + javassistParser_class_name.substring(lastIndexOf + 1) + "_" + clazz.getSimpleName();
        final CtClass cc = ClassPool.getDefault().makeClass(implJavassistParser_class_name);
        //添加泛型
        SignatureAttribute.ClassSignature class_cs = new SignatureAttribute.ClassSignature(null, null, new SignatureAttribute.ClassType[]{
                new SignatureAttribute.ClassType(javassistParser_class_name, new SignatureAttribute.TypeArgument[]{
                        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(clazzName))
                })
        });
        cc.setGenericSignature(class_cs.encode());

        StringBuilder initBody = new StringBuilder();
        //加parser字段
        final String parserClassName = Parser.class.getName();
        cc.addField(CtField.make("public " + parserClassName + " parser;", cc));
        //初始化parser字段
        final CtClass parser_cc = ClassPool.getDefault().get(parserClassName);
        final CtConstructor constructor = CtNewConstructor.make(new CtClass[]{parser_cc}, null, cc);
        initBody.append("{\n");
        initBody.append("this.parser=$1;\n");
        //加processorClass字段并初始化
        final List<Class> processorClassList = Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getAnnotation(PacketField.class)).filter(Objects::nonNull).map(PacketField::processorClass).filter(e -> e != void.class).collect(Collectors.toList());
        for (Class processorClass : processorClassList) {
            final String processorClassName = processorClass.getName();
            final String processorVarName = JavassistUtil.toFirstLowerCase(processorClass.getSimpleName());
            cc.addField(CtField.make("public " + processorClassName + " " + processorVarName + ";", cc));
            initBody.append(JavassistUtil.format("this.{}=new {}();\n", processorVarName, processorClassName));
            initBody.append(JavassistUtil.format("this.{}.parser=$1;\n", processorVarName));
        }
        initBody.append("}\n");
        constructor.setBody(initBody.toString());
        cc.addConstructor(constructor);
        //添加实现、定义parse方法
        final CtClass super_cc = ClassPool.getDefault().get(javassistParser_class_name);
        cc.addInterface(super_cc);
        final CtMethod parse_cm = CtNewMethod.make(
                /**
                 * 在这里定义返回值为Object类型
                 * 因为正常的继承、asm实现方法需要额外创建一个桥接方法、针对泛型部分的参数为Object类型
                 */
                ClassPool.getDefault().get(Object.class.getName()),
                "parse",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(FieldProcessContext.class.getName())
                }, null, null, cc);

        cc.addMethod(parse_cm);

        //添加方法体
        String instanceVarName = "$3";
        StringBuilder body = new StringBuilder();
        body.append("{\n");
        JavassistUtil.append(body, "{} {}=new {}();\n", clazzName, instanceVarName, clazzName);
        BuilderContext context = new BuilderContext(body, this, cc, instanceVarName, null);
        //根据字段构造代码
        buildParseMethodBody(clazz, context);
        JavassistUtil.append(body, "return {};\n", instanceVarName);
        body.append("}\n");
        logger.info(body.toString());
        parse_cm.setBody(body.toString());
        if (generateClassField) {
            cc.writeFile("src/main/java");
        }
        return cc.toClass(JavassistParser.class);
    }


    public final <T> T parse(Class<T> clazz, ByteBuf data, FieldProcessContext processContext) {
        JavassistParser<T> javassistParser = beanClass_to_javassistParser.get(clazz);
        if (javassistParser == null) {
            synchronized (beanClass_to_javassistParser) {
                javassistParser = beanClass_to_javassistParser.get(clazz);
                if (javassistParser == null) {
                    try {
                        final Class impl = buildClass(clazz);
                        logger.info("build Impl [{}] succeed", impl.getName());
                        javassistParser = (JavassistParser<T>) (impl.getConstructor(Parser.class).newInstance(this));
                        beanClass_to_javassistParser.put(clazz, javassistParser);
                    } catch (Exception e) {
                        throw BaseRuntimeException.getException(e);
                    }
                }
            }
        }
        return javassistParser.parse(data, processContext);
    }
}

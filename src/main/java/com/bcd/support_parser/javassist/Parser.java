package com.bcd.support_parser.javassist;

import com.bcd.support_parser.anno.*;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.javassist.builder.*;
import com.bcd.support_parser.javassist.parser.JavassistParser;
import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import com.bcd.support_parser.javassist.util.JavassistUtil;
import io.netty.buffer.ByteBuf;
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
 * {@link #parse(Class, ByteBuf, FieldProcessContext)}
 * <p>
 * 性能表现:
 * 由于是字节码增强技术、和手动编写代码解析效率一样
 */
public class Parser {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final FieldBuilder__F_bean fieldBuilder__f_bean = new FieldBuilder__F_bean();
    public final FieldBuilder__F_bean_list fieldBuilder__f_bean_list = new FieldBuilder__F_bean_list();
    public final FieldBuilder__F_date fieldBuilder__f_date = new FieldBuilder__F_date();
    public final FieldBuilder__F_float_array fieldBuilder__f_float_array = new FieldBuilder__F_float_array();
    public final FieldBuilder__F_float fieldBuilder__f_float_ = new FieldBuilder__F_float();
    public final FieldBuilder__F_integer_array fieldBuilder__f_integer_array = new FieldBuilder__F_integer_array();
    public final FieldBuilder__F_integer fieldBuilder__f_integer_ = new FieldBuilder__F_integer();
    public final FieldBuilder__F_skip fieldBuilder__f_skip = new FieldBuilder__F_skip();
    public final FieldBuilder__F_string fieldBuilder__f_string = new FieldBuilder__F_string();
    public final FieldBuilder__F_userDefine fieldBuilder__f_userDefine = new FieldBuilder__F_userDefine();

    public final Set<Class> annoSet = new HashSet<>();

    {
        annoSet.add(F_integer.class);
        annoSet.add(F_integer_array.class);

        annoSet.add(F_float.class);
        annoSet.add(F_float_array.class);

        annoSet.add(F_string.class);

        annoSet.add(F_date.class);

        annoSet.add(F_bean.class);
        annoSet.add(F_bean_list.class);

        annoSet.add(F_userDefine.class);

        annoSet.add(F_skip.class);
    }


    private final Map<Class, JavassistParser> beanClass_to_javassistParser = new HashMap<>();

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
    private Map<String, int[]> calcBitField(List<Field> fieldList, BuilderContext builderContext) {
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
            final F_float f_float = field.getAnnotation(F_float.class);
            if (f_float != null && f_float.bit() > 0) {
                final int[] ints = new int[]{bitSum, 0, 0};
                tempList.add(ints);
                fieldNameToBitInfo.put(field.getName(), ints);
                bitSum += f_float.len();
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

    private boolean needParse(Field field) {
        final Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annoSet.contains(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public final void buildParseMethodBody(Class clazz, BuilderContext context) {
        //过滤掉 final、static关键字修饰、@F_not注解修饰、且不是public的字段
        final List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(e ->
                        needParse(e) &&
                                !Modifier.isFinal(e.getModifiers()) &&
                                !Modifier.isStatic(e.getModifiers()) &&
                                Modifier.isPublic(e.getModifiers()))
                .collect(Collectors.toList());
        //需要提前计算出F_integer_bit、F_float_bit的占用字节数、以及各个字段的在其中的偏移量
        context.fieldNameToBitInfo = calcBitField(fieldList, context);

        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;

            final F_integer f_integer = field.getAnnotation(F_integer.class);
            if (f_integer != null) {
                fieldBuilder__f_integer_.build(context);
                continue;
            }

            final F_float f_float = field.getAnnotation(F_float.class);
            if (f_float != null) {
                fieldBuilder__f_float_.build(context);
                continue;
            }

            final F_integer_array f_integer_array = field.getAnnotation(F_integer_array.class);
            if (f_integer_array != null) {
                fieldBuilder__f_integer_array.build(context);
                continue;
            }

            final F_float_array f_float_array = field.getAnnotation(F_float_array.class);
            if (f_float_array != null) {
                fieldBuilder__f_float_array.build(context);
                continue;
            }

            final F_string f_string = field.getAnnotation(F_string.class);
            if (f_string != null) {
                fieldBuilder__f_string.build(context);
                continue;
            }

            final F_date f_date = field.getAnnotation(F_date.class);
            if (f_date != null) {
                fieldBuilder__f_date.build(context);
                continue;
            }

            final F_bean f_bean = field.getAnnotation(F_bean.class);
            if (f_bean != null) {
                fieldBuilder__f_bean.build(context);
                continue;
            }

            final F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
            if (f_bean_list != null) {
                fieldBuilder__f_bean_list.build(context);
                continue;
            }

            final F_userDefine f_userDefine = field.getAnnotation(F_userDefine.class);
            if (f_userDefine != null) {
                fieldBuilder__f_userDefine.build(context);
            }

            final F_skip f_skip = field.getAnnotation(F_skip.class);
            if (f_skip != null) {
                fieldBuilder__f_skip.build(context);
                continue;
            }
        }
    }

    public final void buildAppend(StringBuilder body, Class clazz, String fieldVarName, Parser parser, BuilderContext fieldBuilderContext) {
        BuilderContext builderContext = new BuilderContext(body, parser, fieldBuilderContext.implCc, fieldVarName, fieldBuilderContext);
        buildParseMethodBody(clazz, builderContext);
    }

    public final void buildAppend(BuilderContext fieldBuilderContext) {
        final Class clazz = fieldBuilderContext.field.getType().getClass();
        String fieldVarName = JavassistUtil.getFieldVarName(fieldBuilderContext);
        BuilderContext builderContext = new BuilderContext(fieldBuilderContext.body, fieldBuilderContext.parser, fieldBuilderContext.implCc, fieldVarName, fieldBuilderContext);
        buildParseMethodBody(clazz, builderContext);
    }

    public final Class buildClass(Class clazz) throws CannotCompileException, NotFoundException, IOException {
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

        cc.setModifiers(Modifier.FINAL | Modifier.PUBLIC);

        StringBuilder initBody = new StringBuilder();
        //加parser字段
        final String parserClassName = Parser.class.getName();
        cc.addField(CtField.make("private final " + parserClassName + " parser;", cc));
        //初始化parser字段
        final CtClass parser_cc = ClassPool.getDefault().get(parserClassName);
        final CtConstructor constructor = CtNewConstructor.make(new CtClass[]{parser_cc}, null, cc);
        initBody.append("{\n");
        initBody.append("this.parser=$1;\n");
        //加processorClass字段并初始化
        final List<Class> processorClassList = Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getAnnotation(F_userDefine.class)).filter(Objects::nonNull).map(F_userDefine::processorClass).filter(e -> e != void.class).collect(Collectors.toList());
        for (Class processorClass : processorClassList) {
            final String processorClassName = processorClass.getName();
            final String processorVarName = JavassistUtil.toFirstLowerCase(processorClass.getSimpleName());
            cc.addField(CtField.make("private final " + processorClassName + " " + processorVarName + ";", cc));
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
        body.append("\n{\n");
        JavassistUtil.append(body, "final {} {}=new {}();\n", clazzName, instanceVarName, clazzName);
        BuilderContext context = new BuilderContext(body, this, cc, instanceVarName, null);
        //根据字段构造代码
        buildParseMethodBody(clazz, context);
        JavassistUtil.append(body, "return {};\n", instanceVarName);
        body.append("}");
        logger.info(body.toString());
        parse_cm.setBody(body.toString());
        if (generateClassField) {
            cc.writeFile("src/main/java");
        }
//        return cc.toClass(JavassistParser.class);
        return cc.toClass();
    }


    public final <T> T parse(Class<T> clazz, ByteBuf data, FieldProcessContext processContext) {
        JavassistParser<T> javassistParser = beanClass_to_javassistParser.get(clazz);
        if (javassistParser == null) {
            synchronized (beanClass_to_javassistParser) {
                javassistParser = beanClass_to_javassistParser.get(clazz);
                if (javassistParser == null) {
                    try {
                        final Class impl = buildClass(clazz);
                        logger.info("build Impl [{}] succeed\n\n", impl.getName());
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

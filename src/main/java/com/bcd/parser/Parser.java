package com.bcd.parser;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.info.PacketInfo;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import com.bcd.parser.processer.impl.*;
import com.bcd.parser.util.ClassUtil;
import com.bcd.parser.util.ParserUtil;
import com.bcd.parser.util.RpnUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;


/**
 * 协议解析器
 * 主要功能如下:
 * 1、解析
 * 从{@link ByteBuf}中获取二进制数据、解析成{@link Parsable}的class对象实例
 * 解析规则参照{@link com.bcd.parser.anno.PacketField}
 *
 * 2、反解析
 * 将{@link Parsable}的class对象实例反解析成为ByteBuf二进制数据流
 * 反解析规则参照{@link com.bcd.parser.anno.PacketField}、注意反解析不支持{@link PacketField#valExpr()}
 *
 * {@link FieldProcessor}说明:
 * {@link FieldProcessor}是针对每个字段类型定义的处理器、所有需要解析的字段类型必须要有对应的处理器
 * 目前大致分为如下2类:
 * 1、默认基础解析器、针对一些常用的字段类型、具体查看{@link #baseProcessorList}
 * 2、自定义扩展解析器、通过重写{@link #initExtProcessor()}方法集成进来、这些解析器使用在{@link PacketField#processorClass()}上
 *
 * {@link PacketInfo}说明:
 * {@link PacketInfo}是所有{@link Parsable}的class转换成的描述信息、被解析的类必须标注{@link Parsable}注解
 *
 * 性能表现:
 * 以gb32960协议为例子
 * cpu: Intel(R) Core(TM) i5-7360U CPU @ 2.30GHz
 * 单线程、在cpu使用率90%+ 的情况下
 * 解析速度约为 25-26w/s、多个线程成倍数增长
 * 反解析速度约为 18-19w/s、多个线程成倍数增长
 * 具体查看{@link com.bcd.parser.impl.gb32960.Parser_gb32960#main(String[])}
 * 注意:
 * 因为是cpu密集型运算、所以性能达到计算机物理核心个数后已经达到上限、不能以逻辑核心为准、此时虽然整体cpu使用率没有满、但这只是使用率显示问题
 * 例如 2核4线程 、物理核心2个、逻辑核心4个、此时使用2个线程就能用尽cpu资源、即使指标显示cpu使用率50%、其实再加线程已经没有提升
 *
 * 遗留问题:
 * 1、如果当一个字段需要作为变量供其他表达式使用、且此时变量解析出来的值为无效或者异常、会导致解析出错;
 *    要解决这个问题需要设置字段自定义处理器{@link PacketField#processorClass()}
 *
 * 注意事项:
 * 1、在使用时候需要定义字段类型 能完全 容纳下协议文档中所占用字节数
 *   (包括容纳下异常值、无效值,这两种值一般是0xfe、0xff结尾; 例如两个字节即为0xfffe、0xffff、此时需要用int存储才能正确表示其值)
 *
 */
@SuppressWarnings("unchecked")
public abstract class Parser {

    public final Logger logger= LoggerFactory.getLogger(this.getClass());
    public final Map<Class, PacketInfo> packetInfoCache =new HashMap<>();

    /**
     * 处理器数组
     */
    public FieldProcessor[] fieldProcessors;

    /**
     * 基础处理器,内置
     */
    protected List<FieldProcessor> baseProcessorList;
    protected FieldProcessor<Byte> byteProcessor=new ByteProcessor();
    protected FieldProcessor<Short> shortProcessor=new ShortProcessor();
    protected FieldProcessor<Integer> integerProcessor=new IntegerProcessor();
    protected FieldProcessor<Long> longProcessor=new LongProcessor();
    protected FieldProcessor<Float> floatProcessor=new FloatProcessor();
    protected FieldProcessor<Double> doubleProcessor=new DoubleProcessor();
    protected FieldProcessor<byte[]> byteArrayProcessor=new ByteArrayProcessor();
    protected FieldProcessor<short[]> shortArrayProcessor=new ShortArrayProcessor();
    protected FieldProcessor<int[]> integerArrayProcessor=new IntegerArrayProcessor();
    protected FieldProcessor<long[]> longArrayProcessor=new LongArrayProcessor();
    protected FieldProcessor<float[]> floatArrayProcessor=new FloatArrayProcessor();
    protected FieldProcessor<double[]> doubleArrayProcessor=new DoubleArrayProcessor();
    protected FieldProcessor<String> stringProcessor=new StringProcessor();
    protected FieldProcessor<Date> dateProcessor=new DateProcessor();
    protected FieldProcessor<ByteBuf> byteBufProcessor=new ByteBufProcessor();
    protected FieldProcessor<List> listProcessor=new ListProcessor();
    protected FieldProcessor<Object> parsableObjectProcessor=new ParsableObjectProcessor();

    protected boolean printStack=false;

    public void init(){
        //初始化处理器
        initProcessor();
        //初始化解析实体对象
        initPacketInfo();
        //设置处理器
        afterInit();
    }

    private void afterInit(){
        //为每个处理器绑定当前解析器
        for (FieldProcessor processor : fieldProcessors) {
            processor.setParser(this);
        }
    }

    private void initProcessor(){
        List<FieldProcessor> processorList=new ArrayList<>();
        baseProcessorList=initBaseProcessor();
        processorList.addAll(baseProcessorList);
        processorList.addAll(initExtProcessor());
        fieldProcessors=processorList.toArray(new FieldProcessor[0]);
        if(logger.isInfoEnabled()){
            logger.info("init processor succeed,follow list:\n{}", Arrays.stream(fieldProcessors).map(e->e.getClass().getName()).reduce((e1,e2)->e1+"\n"+e2).orElse(""));
        }
    }

    /**
     * 初始化基础处理器
     */
    private List<FieldProcessor> initBaseProcessor(){
        List<FieldProcessor> processorList=new ArrayList<>();
        processorList.add(this.byteProcessor);
        processorList.add(this.shortProcessor);
        processorList.add(this.integerProcessor);
        processorList.add(this.longProcessor);
        processorList.add(this.floatProcessor);
        processorList.add(this.doubleProcessor);
        processorList.add(this.byteArrayProcessor);
        processorList.add(this.shortArrayProcessor);
        processorList.add(this.integerArrayProcessor);
        processorList.add(this.longArrayProcessor);
        processorList.add(this.floatArrayProcessor);
        processorList.add(this.doubleArrayProcessor);
        processorList.add(this.stringProcessor);
        processorList.add(this.dateProcessor);
        processorList.add(this.byteBufProcessor);
        processorList.add(this.listProcessor);
        processorList.add(this.parsableObjectProcessor);
        return processorList;
    }

    /**
     * 初始化额外处理器
     * 主要是自定义处理器
     */
    protected List<FieldProcessor> initExtProcessor(){
        return Collections.emptyList();
    }

    /**
     * 初始化 classToHandler
     * 通过spring
     */
//    protected List<FieldProcessor> initProcessorBySpring(){
//        List<FieldProcessor> processorList=new ArrayList<>();
//        SpringUtil.applicationContext.getBeansOfType(FieldProcessor.class).values().forEach(e->{
//            processorList.add(e);
//        });
//        return processorList;
//    }

    /**
     * 扫描包下面所有{@link FieldProcessor}的子类、去除接口和抽象类
     * 去除属于基础处理器类型 {@link #baseProcessorList}
     * 实例化这些类对象(这些class必须都有空参构造方法)
     * @param pkg
     * @return
     */
    protected List<FieldProcessor> initProcessorByScanClass(String pkg){
        List<FieldProcessor> processorList=new ArrayList<>();
        try {
            A:for (Class e : ClassUtil.getClassesByParentClass(FieldProcessor.class, pkg)) {
                for (FieldProcessor base : baseProcessorList) {
                    if(e==base.getClass()){
                        continue A;
                    }
                }
                processorList.add((FieldProcessor) e.newInstance());
            }
        } catch (IOException | ClassNotFoundException |IllegalAccessException |InstantiationException e) {
            throw BaseRuntimeException.getException(e);
        }
        return processorList;
    }

    /**
     * 初始化{@link #packetInfoCache}
     */
    private void initPacketInfo(){
        List<Class> classes= getParsableClass();
        for (Class clazz : classes) {
            packetInfoCache.put(clazz, ParserUtil.toPacketInfo(clazz,fieldProcessors));
        }
        if(logger.isInfoEnabled()){
            logger.info("init packetInfo succeed,follow list:\n{}", packetInfoCache.values().stream().map(e->e.getClazz().getName()).reduce((e1,e2)->e1+"\n"+e2).orElse(""));
        }
    }

    /**
     * 加载所有 {@link Parsable} 注解的类
     */
    protected abstract List<Class> getParsableClass();

    /**
     * 解析对象
     * @param clazz 解析class类
     * @param data 解析数据源
     * @param <T> 结果对象类型
     * @return
     */
    public final <T>T parse(Class<T> clazz, ByteBuf data){
        return parse(clazz,data,null);
    }

    /**
     * 解析{@link com.bcd.parser.anno.PacketField}字段
     * @param packetInfo 当前class对应的{@link PacketInfo}
     * @param data 解析ByteBuf数据源
     * @param instance 当前class对象实例
     * @param parentContext 当前解析所属环境
     * @throws IllegalAccessException
     */
    private void parsePacketField(PacketInfo packetInfo, ByteBuf data,Object instance,FieldProcessContext parentContext) throws IllegalAccessException {
        //进行解析
        int varValArrLen=packetInfo.getVarValArrLen();
        int varValArrOffset=packetInfo.getVarValArrOffset();
        int [] vals=varValArrLen==0?null:new int[varValArrLen];
        FieldProcessContext processContext=new FieldProcessContext();
        processContext.setParentContext(parentContext);
        processContext.setInstance(instance);
        FieldInfo[] fieldInfos= packetInfo.getFieldInfos();
        for(int i=0,end=fieldInfos.length;i<end;i++){
            FieldInfo fieldInfo=fieldInfos[i];
            /**
             * 代表 {@link PacketField#lenExpr()}
             */
            Object[] lenRpn= fieldInfo.getLenRpn();
            /**
             * 代表 {@link PacketField#listLenExpr()}
             */
            Object[] listLenRpn= fieldInfo.getListLenRpn();

            int len;
            if(lenRpn==null){
                len=fieldInfo.getPacketField_len();
            }else{
                if(lenRpn.length==1){
                    len=vals[(char)lenRpn[0]-varValArrOffset];
                }else {
                    len = RpnUtil.calcRPN_char_int(lenRpn, vals,varValArrOffset);
                }
            }

            int listLen;
            if(listLenRpn!=null){
                if(listLenRpn.length==1){
                    listLen=vals[(char)listLenRpn[0]-varValArrOffset];
                }else {
                    listLen = RpnUtil.calcRPN_char_int(listLenRpn, vals,varValArrOffset);
                }
                processContext.setListLen(listLen);
            }

            processContext.setLen(len);
            processContext.setFieldInfo(fieldInfo);

            Object val;
            //过滤掉对象的日志
            if(printStack
//                    &&fieldInfo.getProcessorIndex()!=16
            ){
                int startIndex=data.readerIndex();
                val=fieldProcessors[fieldInfo.getProcessorIndex()].process(data,processContext);
                int endIndex=data.readerIndex();
                byte[] arr=new byte[endIndex-startIndex];
                data.getBytes(startIndex,arr);
                if(fieldInfo.getPacketField_valExpr().isEmpty()){
                    logger.info("parse class[{}] field[{}] val[{}] hex[{}] parser[{}]",
                            packetInfo.getClazz().getName(),
                            fieldInfo.getField().getName(),
                            val,
                            ByteBufUtil.hexDump(arr),
                            fieldProcessors[fieldInfo.getProcessorIndex()].getClass().getName());
                }else {

                    logger.info("parse class[{}] field[{}] valExpr[{}] valExprPrecision[{}] val[{}] hex[{}] parser[{}]",
                            packetInfo.getClazz().getName(),
                            fieldInfo.getField().getName(),
                            fieldInfo.getPacketField_valExpr(),

                            fieldInfo.getValExprPrecision(),
                            val,
                            ByteBufUtil.hexDump(arr),
                            fieldProcessors[fieldInfo.getProcessorIndex()].getClass().getName());
                }
            }else{
                val=fieldProcessors[fieldInfo.getProcessorIndex()].process(data,processContext);
            }
            if(fieldInfo.isVar()){
                vals[fieldInfo.getPacketField_var_int()-varValArrOffset]=((Number)val).intValue();
            }
            fieldInfo.getField().set(instance,val);
        }
    }

    /**
     * 根据类型和缓冲数据生成对应对象
     * 所有涉及解析对象必须有空参数的构造方法
     * @param clazz
     * @param data
     * @param parentContext 当前对象作为其他类的字段解析时候的环境、顶层环境传入null
     * @param <T>
     * @return
     */
    public final <T>T parse(Class<T> clazz, ByteBuf data, FieldProcessContext parentContext){
        //解析包
        PacketInfo packetInfo=packetInfoCache.get(clazz);
        if(packetInfo==null){
            throw BaseRuntimeException.getException("can not find class["+clazz.getName()+"] packetInfo");
        }
        try {
            //构造实例
            T instance= clazz.newInstance();
            /**
             * 解析{@link com.bcd.parser.anno.PacketField}字段
             */
            parsePacketField(packetInfo,data,instance,parentContext);
            return instance;
        } catch (IllegalAccessException| InstantiationException e) {
            throw BaseRuntimeException.getException(e);
        }
    }

    /**
     * 将对象转换为byteBuf
     * @param t 不能为null
     * @param res 结果接收容器
     */
    public final void deParse(Object t, ByteBuf res){
        deParse(t, res,null);
    }

    /**
     * 将对象转换为byteBuf
     * @param t 不能为null
     * @param res 结果接收容器
     * @param parentContext 反解析环境
     */
    public final void deParse(Object t, ByteBuf res, FieldDeProcessContext parentContext){
        try{
            if(res==null){
                res= Unpooled.buffer();
            }
            Class clazz= t.getClass();
            PacketInfo packetInfo=packetInfoCache.get(clazz);
            //进行解析
            int [] vals=null;
            int varValArrLen=packetInfo.getVarValArrLen();
            int varValArrOffset=packetInfo.getVarValArrOffset();
            if(varValArrLen!=0){
                vals=new int[varValArrLen];
            }
            FieldDeProcessContext processContext=new FieldDeProcessContext();
            processContext.setInstance(t);
            processContext.setParentContext(parentContext);
            for (FieldInfo fieldInfo : packetInfo.getFieldInfos()) {
                int processorIndex=fieldInfo.getProcessorIndex();
                Object data=fieldInfo.getField().get(t);
                /**
                 * 代表 {@link PacketField#lenExpr()}
                 */
                Object[] lenRpn= fieldInfo.getLenRpn();
                /**
                 * 代表 {@link PacketField#listLenExpr()}
                 */
                Object[] lisLlenRpn= fieldInfo.getListLenRpn();
                int len;
                int listLen=0;
                if(lenRpn==null){
                    len=fieldInfo.getPacketField_len();
                }else{
                    if(lenRpn.length==1){
                        len=vals[(char)lenRpn[0]-varValArrOffset];
                    }else {
                        len = RpnUtil.calcRPN_char_int(lenRpn, vals,varValArrOffset);
                    }
                }
                if(lisLlenRpn!=null){
                    if(lisLlenRpn.length==1){
                        listLen=vals[(char)lisLlenRpn[0]-varValArrOffset];
                    }else {
                        listLen = RpnUtil.calcRPN_char_int(lisLlenRpn, vals,varValArrOffset);
                    }
                }
                processContext.setFieldInfo(fieldInfo);
                processContext.setLen(len);
                processContext.setListLen(listLen);

                if(printStack
//                        &&fieldInfo.getProcessorIndex()!=16
                ){
                    int startIndex = res.writerIndex();
                    fieldProcessors[processorIndex].deProcess(data, res, processContext);
                    int endIndex = res.writerIndex();
                    byte[] arr = new byte[endIndex - startIndex];
                    res.getBytes(startIndex, arr);
                    if (fieldInfo.getPacketField_valExpr().isEmpty()) {
                        logger.info("deParse class[{}] field[{}] val[{}] hex[{}] parser[{}]",
                                packetInfo.getClazz().getName(),
                                fieldInfo.getField().getName(),
                                data,
                                ByteBufUtil.hexDump(arr),
                                fieldProcessors[processorIndex].getClass().getName());
                    } else {
                        String reverseValExpr=RpnUtil.parseRPNToArithmetic(fieldInfo.getReverseValRpn());
                        logger.info("deParse class[{}] field[{}] valExpr[{}] reverseValExpr[{}] valExprPrecision[{}] val[{}] hex[{}] parser[{}]",
                                packetInfo.getClazz().getName(),
                                fieldInfo.getField().getName(),
                                fieldInfo.getPacketField_valExpr(),
                                reverseValExpr,
                                fieldInfo.getValExprPrecision(),
                                data,
                                ByteBufUtil.hexDump(arr),
                                fieldProcessors[processorIndex].getClass().getName());
                    }
                }else{
                    fieldProcessors[processorIndex].deProcess(data,res,processContext);
                }

                if(fieldInfo.isVar()){
                    vals[fieldInfo.getPacketField_var()-varValArrOffset]=((Number)data).intValue();
                }
            }
        } catch (IllegalAccessException e) {
            throw BaseRuntimeException.getException(e);
        }
    }

    public final <T>String toHex(T t){
        ByteBuf byteBuf= toByteBuf(t);
        if(byteBuf==null){
            return "";
        }else{
            return ByteBufUtil.hexDump(byteBuf);
        }
    }

    public final ByteBuf toByteBuf(Object t){
        if(t==null){
            return null;
        }else {
            ByteBuf res = Unpooled.buffer();
            deParse(t, res);
            return res;
        }
    }

    public Map<Class, PacketInfo> getPacketInfoCache() {
        return packetInfoCache;
    }

    public FieldProcessor[] getFieldProcessors() {
        return fieldProcessors;
    }

    public void setFieldProcessors(FieldProcessor[] fieldProcessors) {
        this.fieldProcessors = fieldProcessors;
    }

    public List<FieldProcessor> getBaseProcessorList() {
        return baseProcessorList;
    }

    public void setBaseProcessorList(List<FieldProcessor> baseProcessorList) {
        this.baseProcessorList = baseProcessorList;
    }

    public FieldProcessor<Byte> getByteProcessor() {
        return byteProcessor;
    }

    public void setByteProcessor(FieldProcessor<Byte> byteProcessor) {
        this.byteProcessor = byteProcessor;
    }

    public FieldProcessor<Short> getShortProcessor() {
        return shortProcessor;
    }

    public void setShortProcessor(FieldProcessor<Short> shortProcessor) {
        this.shortProcessor = shortProcessor;
    }

    public FieldProcessor<Integer> getIntegerProcessor() {
        return integerProcessor;
    }

    public void setIntegerProcessor(FieldProcessor<Integer> integerProcessor) {
        this.integerProcessor = integerProcessor;
    }

    public FieldProcessor<Long> getLongProcessor() {
        return longProcessor;
    }

    public void setLongProcessor(FieldProcessor<Long> longProcessor) {
        this.longProcessor = longProcessor;
    }

    public FieldProcessor<Float> getFloatProcessor() {
        return floatProcessor;
    }

    public void setFloatProcessor(FieldProcessor<Float> floatProcessor) {
        this.floatProcessor = floatProcessor;
    }

    public FieldProcessor<Double> getDoubleProcessor() {
        return doubleProcessor;
    }

    public void setDoubleProcessor(FieldProcessor<Double> doubleProcessor) {
        this.doubleProcessor = doubleProcessor;
    }

    public FieldProcessor<byte[]> getByteArrayProcessor() {
        return byteArrayProcessor;
    }

    public void setByteArrayProcessor(FieldProcessor<byte[]> byteArrayProcessor) {
        this.byteArrayProcessor = byteArrayProcessor;
    }

    public FieldProcessor<short[]> getShortArrayProcessor() {
        return shortArrayProcessor;
    }

    public void setShortArrayProcessor(FieldProcessor<short[]> shortArrayProcessor) {
        this.shortArrayProcessor = shortArrayProcessor;
    }

    public FieldProcessor<int[]> getIntegerArrayProcessor() {
        return integerArrayProcessor;
    }

    public void setIntegerArrayProcessor(FieldProcessor<int[]> integerArrayProcessor) {
        this.integerArrayProcessor = integerArrayProcessor;
    }

    public FieldProcessor<long[]> getLongArrayProcessor() {
        return longArrayProcessor;
    }

    public void setLongArrayProcessor(FieldProcessor<long[]> longArrayProcessor) {
        this.longArrayProcessor = longArrayProcessor;
    }

    public FieldProcessor<String> getStringProcessor() {
        return stringProcessor;
    }

    public void setStringProcessor(FieldProcessor<String> stringProcessor) {
        this.stringProcessor = stringProcessor;
    }

    public FieldProcessor<Date> getDateProcessor() {
        return dateProcessor;
    }

    public void setDateProcessor(FieldProcessor<Date> dateProcessor) {
        this.dateProcessor = dateProcessor;
    }

    public FieldProcessor<ByteBuf> getByteBufProcessor() {
        return byteBufProcessor;
    }

    public void setByteBufProcessor(FieldProcessor<ByteBuf> byteBufProcessor) {
        this.byteBufProcessor = byteBufProcessor;
    }

    public FieldProcessor<List> getListProcessor() {
        return listProcessor;
    }

    public void setListProcessor(FieldProcessor<List> listProcessor) {
        this.listProcessor = listProcessor;
    }

    public FieldProcessor<Object> getParsableObjectProcessor() {
        return parsableObjectProcessor;
    }

    public void setParsableObjectProcessor(FieldProcessor<Object> parsableObjectProcessor) {
        this.parsableObjectProcessor = parsableObjectProcessor;
    }

    public FieldProcessor<float[]> getFloatArrayProcessor() {
        return floatArrayProcessor;
    }

    public void setFloatArrayProcessor(FieldProcessor<float[]> floatArrayProcessor) {
        this.floatArrayProcessor = floatArrayProcessor;
    }

    public FieldProcessor<double[]> getDoubleArrayProcessor() {
        return doubleArrayProcessor;
    }

    public void setDoubleArrayProcessor(FieldProcessor<double[]> doubleArrayProcessor) {
        this.doubleArrayProcessor = doubleArrayProcessor;
    }

    public boolean isPrintStack() {
        return printStack;
    }

    public void setPrintStack(boolean printStack) {
        this.printStack = printStack;
    }
}

package com.bcd.parser;

import com.bcd.parser.anno.Parsable;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.info.OffsetFieldInfo;
import com.bcd.parser.info.PacketInfo;
import com.bcd.parser.process.FieldDeProcessContext;
import com.bcd.parser.process.FieldProcessContext;
import com.bcd.parser.process.FieldProcessor;
import com.bcd.parser.process.impl.*;
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


@SuppressWarnings("unchecked")
public abstract class Parser {

    Logger logger= LoggerFactory.getLogger(this.getClass());

    public final Map<Class, PacketInfo> packetInfoCache =new HashMap<>();

    /**
     * 处理器数组
     */
    public FieldProcessor[] fieldProcessors;

    /**
     * 启用偏移字段值处理
     */
    protected boolean enableOffsetField=false;

    /**
     * 基础处理器,内置
     */
    protected List<FieldProcessor> baseProcessorList;
    protected FieldProcessor byteProcessor=new ByteProcessor();
    protected FieldProcessor shortProcessor=new ShortProcessor();
    protected FieldProcessor integerProcessor=new IntegerProcessor();
    protected FieldProcessor longProcessor=new LongProcessor();
    protected FieldProcessor floatProcessor=new FloatProcessor();
    protected FieldProcessor doubleProcessor=new DoubleProcessor();
    protected FieldProcessor<byte[]> byteArrayProcessor=new ByteArrayProcessor();
    protected FieldProcessor<short[]> shortArrayProcessor=new ShortArrayProcessor();
    protected FieldProcessor<int[]> integerArrayProcessor=new IntegerArrayProcessor();
    protected FieldProcessor<long[]> longArrayProcessor=new LongArrayProcessor();
    protected FieldProcessor<String> stringProcessor=new StringProcessor();
    protected FieldProcessor<Date> dateProcessor=new DateProcessor();
    protected FieldProcessor<ByteBuf> byteBufProcessor=new ByteBufProcessor();
    protected FieldProcessor<List> listProcessor=new ListProcessor();
    protected FieldProcessor<Object> parsableObjectProcessor=new ParsableObjectProcessor();

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
     * 初始化 classToParser
     * 去除接口和抽象类
     * 通过扫描
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
            packetInfoCache.put(clazz, ParserUtil.toPacketInfo(clazz,enableOffsetField,fieldProcessors));
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
        for (FieldInfo fieldInfo : packetInfo.getFieldInfos()) {
            int processorIndex=fieldInfo.getProcessorIndex();
            /**
             * 代表 {@link PacketField#lenExpr()}
             */
            Object[] lenRpn= fieldInfo.getLenRpn();
            /**
             * 代表 {@link PacketField#listLenExpr()}
             */
            Object[] listLenRpn= fieldInfo.getListLenRpn();
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
            if(listLenRpn!=null){
                if(listLenRpn.length==1){
                    listLen=vals[(char)listLenRpn[0]-varValArrOffset];
                }else {
                    listLen = RpnUtil.calcRPN_char_int(listLenRpn, vals,varValArrOffset);
                }
            }
            processContext.setFieldInfo(fieldInfo);
            processContext.setLen(len);
            processContext.setListLen(listLen);
            Object val=fieldProcessors[processorIndex].process(data,processContext);
            if(fieldInfo.isVar()){
                vals[fieldInfo.getPacketField_var()-varValArrOffset]=((Number)val).intValue();
            }
            fieldInfo.getField().set(instance,val);
        }
    }

    /**
     * 解析{@link com.bcd.parser.anno.OffsetField}字段
     * @param packetInfo 当前class对应的{@link PacketInfo}
     * @param instance 当前class对象实例
     * @throws IllegalAccessException
     */
    private void parseOffsetField(PacketInfo packetInfo,Object instance) throws IllegalAccessException {
        //偏移量值计算
        OffsetFieldInfo[] offsetFieldInfos = packetInfo.getOffsetFieldInfos();
        if (offsetFieldInfos != null && offsetFieldInfos.length>0) {
            for (OffsetFieldInfo offsetFieldInfo : offsetFieldInfos) {
                Object sourceVal = offsetFieldInfo.getSourceField().get(instance);
                double destVal = RpnUtil.calcRPN_char_double_singleVar(offsetFieldInfo.getRpn(),((Number) sourceVal).doubleValue());
                switch (offsetFieldInfo.getFieldType()) {
                    case 1: {
                        offsetFieldInfo.getField().set(instance, (byte)destVal);
                        break;
                    }
                    case 2: {
                        offsetFieldInfo.getField().set(instance, (short)destVal);
                        break;
                    }
                    case 3: {
                        offsetFieldInfo.getField().set(instance, (int)destVal);
                        break;
                    }
                    case 4: {
                        offsetFieldInfo.getField().set(instance, (long)destVal);
                        break;
                    }
                    case 5: {
                        offsetFieldInfo.getField().set(instance, (float)destVal);
                        break;
                    }
                    case 6: {
                        offsetFieldInfo.getField().set(instance, destVal);
                        break;
                    }
                }
            }
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
            if(enableOffsetField) {
                /**
                 * 解析{@link com.bcd.parser.anno.OffsetField}字段
                 */
                parseOffsetField(packetInfo,instance);
            }
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
                fieldProcessors[processorIndex].deProcess(data,res,processContext);
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

    public boolean isEnableOffsetField() {
        return enableOffsetField;
    }

    public void setEnableOffsetField(boolean enableOffsetField) {
        this.enableOffsetField = enableOffsetField;
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

    public FieldProcessor getByteProcessor() {
        return byteProcessor;
    }

    public void setByteProcessor(FieldProcessor byteProcessor) {
        this.byteProcessor = byteProcessor;
    }

    public FieldProcessor getShortProcessor() {
        return shortProcessor;
    }

    public void setShortProcessor(FieldProcessor shortProcessor) {
        this.shortProcessor = shortProcessor;
    }

    public FieldProcessor getIntegerProcessor() {
        return integerProcessor;
    }

    public void setIntegerProcessor(FieldProcessor integerProcessor) {
        this.integerProcessor = integerProcessor;
    }

    public FieldProcessor getLongProcessor() {
        return longProcessor;
    }

    public void setLongProcessor(FieldProcessor longProcessor) {
        this.longProcessor = longProcessor;
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
}

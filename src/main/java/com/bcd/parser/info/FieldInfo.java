package com.bcd.parser.info;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.Parser;
import com.bcd.parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldInfo {

    private Field field;

    private PacketInfo packetInfo;

    /**
     * #{@link PacketField} 属性
     */
    private int packetField_index;
    private int packetField_len;
    private char packetField_var;
    private String packetField_lenExpr;
    private boolean packetField_skip;
    private String packetField_listLenExpr;
    private int packetField_singleLen;
    private Class packetField_parserClass;
    private String packetField_valExpr;
    private int packetField_valPrecision;

    /**
     * packetField_var对应的int、已经减去了{@link PacketInfo#varValArrOffset}
     */
    private int packetField_var_int;

    /**
     * {@link Parser#fieldProcessors} 索引
     * 0、byte/Byte
     * 1、short/Short
     * 2、int/Integer
     * 3、long/Long
     * 4、float/Float
     * 5、double/Double
     * 6、byte[]
     * 7、short[]
     * 8、int[]
     * 9、long[]
     * 10、float[]
     * 11、double[]
     * 12、String
     * 13、Date
     * 14、ByteBuf
     * 15、List<{@link com.bcd.parser.anno.Parsable}>
     * 16、Array[<{@link com.bcd.parser.anno.Parsable}>]
     * 17、@Parsable标注实体类对象
     *
     * >=18、自定义处理器
     *
     */
    private int processorIndex;

    /**
     * {@link PacketField#var()} 属性不为空
     * 只有当
     * {@link FieldInfo#processorIndex} 为数字类型(0、1、2、3)时候,才可能是true
     */
    private boolean isVar;

    /**
     * processorIndex=15时候代表集合泛型
     * processorIndex=16时候代表数组元素类型
     * processorIndex=17代表实体类型
     */
    private Class clazz;

    /**
     * 对应 {@link PacketField#lenExpr()}表达式
     * 其中的变量char 已经减去了{@link PacketInfo#varValArrOffset}
     */
    private Object[] lenRpn;

    /**
     * 对应 {@link PacketField#listLenExpr()}表达式
     * 其中的变量char 已经减去了{@link PacketInfo#varValArrOffset}
     */
    private Object[] listLenRpn;

    /**
     * 对应 {@link PacketField#valExpr()}表达式
     * [a,b]
     */
    private int[] valExpr_int;

    /**
     * {@link sun.misc.Unsafe#objectFieldOffset(Field)} 得出的偏移量
     */
    private long unsafeOffset;

    /**
     * {@link com.bcd.parser.util.UnsafeUtil#fieldType(Field)} 得出类型
     *  字段基础类型、如果不属于java基础类型、则为0
     *  1:byte
     *  2:short
     *  3:int
     *  4:long
     *  5:float
     *  6:double
     *  7:char
     *  8:boolean
     */
    private int unsafeType;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isVar() {
        return isVar;
    }

    public void setVar(boolean var) {
        isVar = var;
    }

    public int getPacketField_index() {
        return packetField_index;
    }

    public int getPacketField_len() {
        return packetField_len;
    }

    public char getPacketField_var() {
        return packetField_var;
    }

    public String getPacketField_lenExpr() {
        return packetField_lenExpr;
    }

    public String getPacketField_listLenExpr() {
        return packetField_listLenExpr;
    }

    public int getPacketField_singleLen() {
        return packetField_singleLen;
    }

    public Class getPacketField_parserClass() {
        return packetField_parserClass;
    }

    public void setPacketField_index(int packetField_index) {
        this.packetField_index = packetField_index;
    }

    public void setPacketField_len(int packetField_len) {
        this.packetField_len = packetField_len;
    }

    public void setPacketField_var(char packetField_var) {
        this.packetField_var = packetField_var;
    }

    public void setPacketField_lenExpr(String packetField_lenExpr) {
        this.packetField_lenExpr = packetField_lenExpr;
    }

    public void setPacketField_listLenExpr(String packetField_listLenExpr) {
        this.packetField_listLenExpr = packetField_listLenExpr;
    }

    public void setPacketField_singleLen(int packetField_singleLen) {
        this.packetField_singleLen = packetField_singleLen;
    }

    public void setPacketField_parserClass(Class packetField_parserClass) {
        this.packetField_parserClass = packetField_parserClass;
    }

    public int getProcessorIndex() {
        return processorIndex;
    }

    public void setProcessorIndex(int processorIndex) {
        this.processorIndex = processorIndex;
    }

    public Object[] getLenRpn() {
        return lenRpn;
    }

    public void setLenRpn(Object[] lenRpn) {
        this.lenRpn = lenRpn;
    }

    public Object[] getListLenRpn() {
        return listLenRpn;
    }

    public void setListLenRpn(Object[] listLenRpn) {
        this.listLenRpn = listLenRpn;
    }

    public String getPacketField_valExpr() {
        return packetField_valExpr;
    }

    public void setPacketField_valExpr(String packetField_valExpr) {
        this.packetField_valExpr = packetField_valExpr;
    }

    public int getPacketField_var_int() {
        return packetField_var_int;
    }

    public void setPacketField_var_int(int packetField_var_int) {
        this.packetField_var_int = packetField_var_int;
    }

    public int getPacketField_valPrecision() {
        return packetField_valPrecision;
    }

    public void setPacketField_valPrecision(int packetField_valPrecision) {
        this.packetField_valPrecision = packetField_valPrecision;
    }

    public PacketInfo getPacketInfo() {
        return packetInfo;
    }

    public void setPacketInfo(PacketInfo packetInfo) {
        this.packetInfo = packetInfo;
    }

    public boolean isPacketField_skip() {
        return packetField_skip;
    }

    public void setPacketField_skip(boolean packetField_skip) {
        this.packetField_skip = packetField_skip;
    }

    public long getUnsafeOffset() {
        return unsafeOffset;
    }

    public void setUnsafeOffset(long unsafeOffset) {
        this.unsafeOffset = unsafeOffset;
    }

    public int getUnsafeType() {
        return unsafeType;
    }

    public void setUnsafeType(int unsafeType) {
        this.unsafeType = unsafeType;
    }

    public int[] getValExpr_int() {
        return valExpr_int;
    }

    public void setValExpr_int(int[] valExpr_int) {
        this.valExpr_int = valExpr_int;
    }

}

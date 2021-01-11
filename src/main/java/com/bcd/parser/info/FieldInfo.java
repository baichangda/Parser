package com.bcd.parser.info;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.Parser;

import java.lang.reflect.Field;

public class FieldInfo {

    public Field field;

    public PacketInfo packetInfo;

    /**
     * #{@link PacketField} 属性
     */
    public int packetField_index;
    public int packetField_len;
    public char packetField_var;
    public String packetField_lenExpr;
    public String packetField_listLenExpr;
    public int packetField_singleLen;
    public Class packetField_parserClass;
    public String packetField_valExpr;
    public int packetField_valExprPrecision;

    //packetField_var对应的int
    public int packetField_var_int;

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
    public int processorIndex;

    /**
     * {@link PacketField#var()} 属性不为空
     * 只有当
     * {@link FieldInfo#processorIndex} 为数字类型(0、1、2、3)时候,才可能是true
     */
    public boolean isVar;

    /**
     * processorIndex=15时候代表集合泛型
     * processorIndex=16时候代表数组元素类型
     * processorIndex=17代表实体类型
     */
    public Class clazz;

    /**
     * 对应 {@link PacketField#lenExpr()}表达式
     */
    public Object[] lenRpn;

    /**
     * 对应 {@link PacketField#listLenExpr()}表达式
     */
    public Object[] listLenRpn;

    /**
     * 对应 {@link PacketField#valExpr()}表达式
     */
    public Object[] valRpn;
    public Object[] reverseValRpn;

    /**
     * 对应 {@link PacketField#valPrecision()}表达式
     */
    public int valPrecision;

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

    public Object[] getValRpn() {
        return valRpn;
    }

    public void setValRpn(Object[] valRpn) {
        this.valRpn = valRpn;
    }

    public int getPacketField_var_int() {
        return packetField_var_int;
    }

    public void setPacketField_var_int(int packetField_var_int) {
        this.packetField_var_int = packetField_var_int;
    }

    public Object[] getReverseValRpn() {
        return reverseValRpn;
    }

    public void setReverseValRpn(Object[] reverseValRpn) {
        this.reverseValRpn = reverseValRpn;
    }

    public int getPacketField_valExprPrecision() {
        return packetField_valExprPrecision;
    }

    public void setPacketField_valExprPrecision(int packetField_valExprPrecision) {
        this.packetField_valExprPrecision = packetField_valExprPrecision;
    }

    public int getValPrecision() {
        return valPrecision;
    }

    public void setValPrecision(int valPrecision) {
        this.valPrecision = valPrecision;
    }

    public PacketInfo getPacketInfo() {
        return packetInfo;
    }

    public void setPacketInfo(PacketInfo packetInfo) {
        this.packetInfo = packetInfo;
    }
}

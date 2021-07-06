package com.bcd.parser.info;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.Parser;
import com.bcd.parser.util.RpnUtil;

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
    public boolean packetField_skipParse;
    public String packetField_listLenExpr;
    public int packetField_singleLen;
    public Class packetField_parserClass;
    public String packetField_valExpr;
    public int packetField_valPrecision;

    /**
     * packetField_var对应的int、已经减去了{@link PacketInfo#varValArrOffset}
     */
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
     * 其中的变量char 已经减去了{@link PacketInfo#varValArrOffset}
     */
    public RpnUtil.Ele_int[] lenRpn;

    /**
     * 对应 {@link PacketField#listLenExpr()}表达式
     * 其中的变量char 已经减去了{@link PacketInfo#varValArrOffset}
     */
    public RpnUtil.Ele_int[] listLenRpn;

    /**
     * 对应 {@link PacketField#valExpr()}表达式
     * [a,b]
     */
    public int[] valExpr_int;

    /**
     * {@link sun.misc.Unsafe#objectFieldOffset(Field)} 得出的偏移量
     */
    public long unsafeOffset;

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
    public int unsafeType;


}

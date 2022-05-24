package com.bcd.support_parser.info;

import com.bcd.support_parser.Parser;
import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.exception.BaseRuntimeException;
import com.bcd.support_parser.util.ExprCase;
import com.bcd.support_parser.util.RpnUtil;
import com.bcd.support_parser.util.UnsafeUtil;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

public final class FieldInfo {

    public final Field field;

    public final PacketInfo packetInfo;

    /**
     * #{@link PacketField} 属性
     */
//    public final int packetField_index;
//    public final char packetField_var;
//    public final String packetField_lenExpr;
//    public final String packetField_listLenExpr;
//    public final Class packetField_processorClass;

    public final int packetField_len;
    public final boolean packetField_skipParse;
    public final int packetField_singleLen;
    public final String packetField_valExpr;
    public final int packetField_valPrecision;

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
     * 15、List<{@link com.bcd.support_parser.anno.Parsable}>
     * 16、Array[<{@link com.bcd.support_parser.anno.Parsable}>]
     * 17、@Parsable标注实体类对象
     * <p>
     * >=18、自定义处理器
     */
    public final int processorIndex;

    /**
     * {@link PacketField#var()} 属性不为空
     * 只有当
     * {@link FieldInfo#processorIndex} 为数字类型(0、1、2、3)时候,才可能是true
     */
    public final boolean isVar;

    /**
     * processorIndex=15时候代表集合泛型
     * processorIndex=16时候代表数组元素类型
     * processorIndex=17代表实体类型
     */
    public final Class clazz;

    /**
     * 对应 {@link PacketField#lenExpr()}表达式
     * 其中的变量char 已经减去了{@link PacketInfo#varValArrOffset}
     */
    public final RpnUtil.Ele_int[] lenRpn;

    /**
     * 对应 {@link PacketField#listLenExpr()}表达式
     * 其中的变量char 已经减去了{@link PacketInfo#varValArrOffset}
     */
    public final RpnUtil.Ele_int[] listLenRpn;

    /**
     * 对应 {@link PacketField#valExpr()}表达式
     */
    public final ExprCase valExprCase;


    /**
     * {@link sun.misc.Unsafe#objectFieldOffset(Field)} 得出的偏移量
     */
    public final long unsafeOffset;
    /**
     * {@link com.bcd.support_parser.util.UnsafeUtil#fieldType(Field)} 得出类型
     * 字段基础类型、如果不属于java基础类型、则为0
     * 1:byte
     * 2:short
     * 3:int
     * 4:long
     * 5:float
     * 6:double
     * 7:char
     * 8:boolean
     */
    public final int unsafeType;

    public FieldInfo(Field field, PacketInfo packetInfo, PacketField packetField, int processorIndex, Class clazz, RpnUtil.Ele_int[] lenRpn, RpnUtil.Ele_int[] listLenRpn, ExprCase valExprCase) {
        this.field = field;
        this.packetInfo = packetInfo;
//        this.packetField_index = packetField.index();
//        this.packetField_var = packetField.var();
//        this.packetField_lenExpr = packetField.lenExpr();
//        this.packetField_listLenExpr = packetField.listLenExpr();
//        this.packetField_processorClass = packetField.processorClass();
        this.packetField_len = packetField.len();
        this.packetField_skipParse = packetField.skipParse();
        this.packetField_singleLen = packetField.singleLen();
        this.packetField_valExpr = packetField.valExpr();
        this.packetField_valPrecision = packetField.valPrecision();
        this.packetField_var_int = packetField.var();
        this.processorIndex = processorIndex;
        this.isVar = packetField.var() != '0';
        this.clazz = clazz;
        this.lenRpn = lenRpn;
        this.listLenRpn = listLenRpn;
        this.valExprCase = valExprCase;
        this.unsafeOffset = UnsafeUtil.fieldOffset(field);
        this.unsafeType = UnsafeUtil.fieldType(field);
    }
}

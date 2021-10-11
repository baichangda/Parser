package com.bcd.parser.util;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.info.PacketInfo;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class ParserUtil {

    static Logger logger = LoggerFactory.getLogger(ParserUtil.class);

    public static byte[] int_to_bytes_big_endian(int data) {
        return new byte[]{
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) (data)
        };
    }

    public static byte[] short_to_bytes_big_endian(short data) {
        return new byte[]{
                (byte) ((data >> 8) & 0xff),
                (byte) (data)
        };
    }

    public static byte[] long_to_bytes_big_endian(long data) {
        return new byte[]{
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) (data)
        };
    }

    public static int bytes_to_int_big_endian(byte[] datas) {
        return ((datas[0] & 0xff) << 24) |
                ((datas[1] & 0xff) << 16) |
                ((datas[2] & 0xff) << 8) |
                (datas[3] & 0xff);
    }

    public static short bytes_to_short_big_endian(byte[] datas) {
        return (short) (
                ((datas[0] & 0xff) << 8) |
                        (datas[1] & 0xff)
        );
    }

    public static long bytes_to_long_big_endian(byte[] datas) {
        return ((long) (datas[0] & 0xff) << 56) |
                ((long) (datas[1] & 0xff) << 48) |
                ((long) (datas[2] & 0xff) << 40) |
                ((long) (datas[3] & 0xff) << 32) |
                ((long) (datas[4] & 0xff) << 24) |
                ((datas[5] & 0xff) << 16) |
                ((datas[6] & 0xff) << 8) |
                (datas[7] & 0xff);
    }

    public static BaseRuntimeException newLenNotSupportException(FieldDeProcessContext processContext) {
        return BaseRuntimeException.getException("class[{}] field[{}] len[{}] not support",
                processContext.fieldInfo.packetInfo.clazz.getName(),
                processContext.fieldInfo.field.getName(),
                processContext.len);
    }

    public static BaseRuntimeException newLenNotSupportException(FieldProcessContext processContext) {
        return BaseRuntimeException.getException("class[{}] field[{}] len[{}] not support",
                processContext.fieldInfo.packetInfo.clazz.getName(),
                processContext.fieldInfo.field.getName(),
                processContext.len);
    }


    public static BaseRuntimeException newSingleLenNotSupportException(FieldDeProcessContext processContext) {
        return BaseRuntimeException.getException("class[{}] field[{}] singleLen[{}] not support",
                processContext.fieldInfo.packetInfo.clazz.getName(),
                processContext.fieldInfo.field.getName(),
                processContext.fieldInfo.packetField_singleLen);
    }

    public static BaseRuntimeException newSingleLenNotSupportException(FieldProcessContext processContext) {
        return BaseRuntimeException.getException("class[{}] field[{}] singleLen[{}] not support",
                processContext.fieldInfo.packetInfo.clazz.getName(),
                processContext.fieldInfo.field.getName(),
                processContext.fieldInfo.packetField_singleLen);
    }

    /**
     * 验证是否是异常、无效值
     *
     * @param val
     * @return true正常、false异常或无效
     */
    public static boolean checkInvalidOrExceptionVal_byte(byte val) {
        return val != (byte) 0xff && val != (byte) 0xfe;
    }

    /**
     * 验证是否是异常、无效值
     *
     * @param val
     * @param len
     * @return true正常、false异常或无效
     */
    public static boolean checkInvalidOrExceptionVal_short(short val, int len) {
        switch (len) {
            case 1: {
                return val != 0xff && val != 0xfe;
            }
            case 2: {
                return val != (short) 0xffff && val != (short) 0xfffe;
            }
            default: {
                throw BaseRuntimeException.getException("param len[{0}] not support", len);
            }
        }
    }

    /**
     * 验证是否是异常、无效值
     *
     * @param val
     * @param len
     * @return true正常、false异常或无效
     */
    public static boolean checkInvalidOrExceptionVal_int(int val, int len) {
        switch (len) {
            case 1: {
                return val != 0xff && val != 0xfe;
            }
            case 2: {
                return val != 0xffff && val != 0xfffe;
            }
            case 3: {
                return val != 0xffffff && val != 0xfffffe;
            }
            case 4: {
                return val != 0xffffffff && val != 0xfffffffe;
            }
            default: {
                throw BaseRuntimeException.getException("param len[{0}] not support", len);
            }
        }
    }

    /**
     * 验证是否是异常、无效值
     *
     * @param val
     * @param len
     * @return true正常、false异常或无效
     */
    public static boolean checkInvalidOrExceptionVal_long(long val, int len) {
        switch (len) {
            case 1: {
                return val != 0xff && val != 0xfe;
            }
            case 2: {
                return val != 0xffff && val != 0xfffe;
            }
            case 3: {
                return val != 0xffffff && val != 0xfffffe;
            }
            case 4: {
                return val != 0xffffffff && val != 0xfffffffe;
            }
            case 5: {
                return val != 0xffffffffffL && val != 0xfffffffffeL;
            }
            case 6: {
                return val != 0xffffffffffffL && val != 0xfffffffffffeL;
            }
            case 7: {
                return val != 0xffffffffffffffL && val != 0xfffffffffffffeL;
            }
            case 8: {
                return val != 0xffffffffffffffffL && val != 0xfffffffffffffffeL;
            }
            default: {
                throw BaseRuntimeException.getException("param len[{0}] not support", len);
            }
        }
    }


    /**
     * 通过扫描包中所有class方式获取所有带{@link Parsable}注解的类
     *
     * @param packageNames
     * @return
     */
    public static List<Class> getParsableClassByScanPackage(String... packageNames) {
        try {
            return ClassUtil.getClassesWithAnno(Parsable.class, packageNames);
        } catch (IOException | ClassNotFoundException e) {
            throw BaseRuntimeException.getException(e);
        }
    }


    /**
     * 解析类转换成包信息
     *
     * @param clazz
     * @param processors
     * @return
     */
    public static PacketInfo toPacketInfo(Class clazz, FieldProcessor[] processors, int[] processorCount) {
        PacketInfo packetInfo = new PacketInfo();
        packetInfo.clazz = clazz;
        //设置无参构造方法
        try {
            packetInfo.constructor = clazz.getConstructor();
            packetInfo.constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw BaseRuntimeException.getException(e);
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        //求出最小var char int和最大var char int
        int[] maxVarInt = new int[1];
        int[] minVarInt = new int[1];
        /**
         * 1、过滤所有带{@link PacketField}的字段
         * 2、将字段按照{@link PacketField#index()}正序
         * 3、将每个字段类型解析成FieldInfo
         */
        packetInfo.fieldInfos = Arrays.stream(declaredFields).filter(field -> field.getAnnotation(PacketField.class) != null).sorted((f1, f2) -> {
            int i1 = f1.getAnnotation(PacketField.class).index();
            int i2 = f2.getAnnotation(PacketField.class).index();
            if (i1 < i2) {
                return -1;
            } else if (i1 > i2) {
                return 1;
            } else {
                return 0;
            }
        }).map(field -> {
            field.setAccessible(true);
            PacketField packetField = field.getAnnotation(PacketField.class);

            /**
             * 检查{@link PacketField#skipParse()}条件
             */
            if (packetField.skipParse() &&
                    (packetField.var() != '0' || (packetField.len() == 0 && packetField.lenExpr().equals("")))) {
                throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] PacketField#skip Not Support");
            }

            Class fieldType = field.getType();
            Class typeClazz = null;
            boolean isVar = false;
            int processorIndex;
            /**
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
             processorList.add(this.parsableObjectListProcessor);
             processorList.add(this.parsableObjectArrayProcessor);
             processorList.add(this.parsableObjectProcessor);
             */
            //判断是否特殊处理
            if (packetField.processorClass() == Void.class) {
                //判断是否是List<Bean>(Bean代表自定义实体类型,不包括Byte、Short、Integer、Long)
                if (packetField.listLenExpr().isEmpty()) {
                    if (Byte.class.isAssignableFrom(fieldType) || Byte.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex = 0;
                    } else if (Short.class.isAssignableFrom(fieldType) || Short.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex = 1;
                    } else if (Integer.class.isAssignableFrom(fieldType) || Integer.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex = 2;
                    } else if (Long.class.isAssignableFrom(fieldType) || Long.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex = 3;
                    } else if (Float.class.isAssignableFrom(fieldType) || Float.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex = 4;
                    } else if (Double.class.isAssignableFrom(fieldType) || Double.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex = 5;
                    } else if (String.class.isAssignableFrom(fieldType)) {
                        processorIndex = 12;
                    } else if (Date.class.isAssignableFrom(fieldType)) {
                        processorIndex = 13;
                    } else if (fieldType.isArray()) {
                        //数组类型
                        Class arrType = fieldType.getComponentType();
                        if (Byte.class.isAssignableFrom(arrType) || Byte.TYPE.isAssignableFrom(arrType)) {
                            processorIndex = 6;
                        } else if (Short.class.isAssignableFrom(arrType) || Short.TYPE.isAssignableFrom(arrType)) {
                            processorIndex = 7;
                        } else if (Integer.class.isAssignableFrom(arrType) || Integer.TYPE.isAssignableFrom(arrType)) {
                            processorIndex = 8;
                        } else if (Long.class.isAssignableFrom(arrType) || Long.TYPE.isAssignableFrom(arrType)) {
                            processorIndex = 9;
                        } else if (Float.class.isAssignableFrom(arrType) || Float.TYPE.isAssignableFrom(arrType)) {
                            processorIndex = 10;
                        } else if (Double.class.isAssignableFrom(arrType) || Double.TYPE.isAssignableFrom(arrType)) {
                            processorIndex = 11;
                        } else {
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] Array Type[" + arrType.getName() + "] Not Support");
                        }
                    } else if (ByteBuf.class.isAssignableFrom(fieldType)) {
                        //ByteBuf类型
                        processorIndex = 14;
                    } else {
                        /**
                         * 带{@link Parsable}注解的实体类
                         */
                        if (fieldType.getAnnotation(Parsable.class) == null) {
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] Bean Type[" + fieldType + "] Not Support,Must have annotation [com.bcd.parser.anno.Parsable]");
                        }
                        typeClazz = fieldType;
                        processorIndex = 17;
                    }
                } else {
                    if (fieldType.isArray()) {
                        typeClazz = fieldType.getComponentType();
                        //检查数组对象类型是否支持解析
                        if (typeClazz.getAnnotation(Parsable.class) != null) {
                            processorIndex = 16;
                        } else {
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] Array Type[" + typeClazz.getName() + "] Not Support");
                        }
                    } else {
                        //实体类型集合
                        typeClazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        if (typeClazz.getAnnotation(Parsable.class) != null) {
                            processorIndex = 15;
                        } else {
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] List Type[" + typeClazz.getName() + "] Not Support");
                        }
                    }
                }
            } else {
                //特殊处理,自定义实体类型
                processorIndex = findProcessorIndexByFieldProcessorClass(packetField.processorClass(), processors);
            }

            //统计各个processor处理字段数量
            if (processorCount != null) {
                processorCount[processorIndex] += 1;
            }

            //转换逆波兰表达式
            RpnUtil.Ele_int[] lenRpn = null;
            RpnUtil.Ele_int[] listLenRpn = null;
            int[] valExpr_int = null;
            if (!packetField.lenExpr().isEmpty()) {
                lenRpn = RpnUtil.to_ele_int(RpnUtil.toRpn(packetField.lenExpr()));
            }
            if (!packetField.listLenExpr().isEmpty()) {
                listLenRpn = RpnUtil.to_ele_int(RpnUtil.toRpn(packetField.listLenExpr()));
            }
            if (!packetField.valExpr().isEmpty()) {
                try {
                    double[] simpleExpr = RpnUtil.toExprVar(packetField.valExpr());
                    valExpr_int = new int[]{(int) simpleExpr[0], (int) simpleExpr[1]};
                } catch (Exception ex) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] valExpr[{}] ot support", clazz.getName(), field.getName(), packetField.valExpr());
                }
            }

            //判断是否变量
            if (packetField.var() != '0') {
                isVar = true;
            }

            //求maxVarInt、minVarInt
            if (lenRpn != null) {
                for (RpnUtil.Ele_int e : lenRpn) {
                    switch (e.type) {
                        case 2:
                        case 3: {
                            if (maxVarInt[0] == 0 || e.val > maxVarInt[0]) {
                                maxVarInt[0] = e.val;
                            }
                            if (minVarInt[0] == 0 || e.val < minVarInt[0]) {
                                minVarInt[0] = e.val;
                            }
                        }
                    }
                }
            }
            if (listLenRpn != null) {
                for (RpnUtil.Ele_int e : listLenRpn) {
                    switch (e.type) {
                        case 2:
                        case 3: {
                            if (maxVarInt[0] == 0 || e.val > maxVarInt[0]) {
                                maxVarInt[0] = e.val;
                            }
                            if (minVarInt[0] == 0 || e.val < minVarInt[0]) {
                                minVarInt[0] = e.val;
                            }
                        }
                    }
                }
            }

            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.packetInfo = packetInfo;
            fieldInfo.field = field;
            fieldInfo.isVar = isVar;
            fieldInfo.clazz = typeClazz;
            fieldInfo.processorIndex = processorIndex;
            fieldInfo.lenRpn = lenRpn;
            fieldInfo.listLenRpn = listLenRpn;
            fieldInfo.valExpr_int = valExpr_int;
            fieldInfo.packetField_index = packetField.index();
            fieldInfo.packetField_len = packetField.len();
            fieldInfo.packetField_lenExpr = packetField.lenExpr();
            fieldInfo.packetField_skipParse = packetField.skipParse();
            fieldInfo.packetField_listLenExpr = packetField.listLenExpr();
            fieldInfo.packetField_singleLen = packetField.singleLen();
            fieldInfo.packetField_var = packetField.var();
            fieldInfo.packetField_var_int = packetField.var();
            fieldInfo.packetField_parserClass = packetField.processorClass();
            fieldInfo.packetField_valExpr = packetField.valExpr();
            fieldInfo.unsafeOffset = UnsafeUtil.fieldOffset(field);
            fieldInfo.unsafeType = UnsafeUtil.fieldType(field);
            return fieldInfo;
        }).toArray(FieldInfo[]::new);

        /**
         * A-Z --> 65-90
         * a-z --> 97-122
         * 将所有的变量减去最小的偏移、使得最小的变量存在数组的第一位
         */
        if (maxVarInt[0] != 0) {
            packetInfo.varValArrLen=maxVarInt[0] - minVarInt[0] + 1;
            packetInfo.varValArrOffset=minVarInt[0];
        }

        //预先将var和表达式中的偏移量算出来、在解析时候不用重复计算
        for (FieldInfo fieldInfo : packetInfo.fieldInfos) {
            int varValArrOffset = fieldInfo.packetInfo.varValArrOffset;
            if (fieldInfo.isVar) {
                fieldInfo.packetField_var_int=fieldInfo.packetField_var_int - varValArrOffset;
            }
            RpnUtil.Ele_int[] lenRpn = fieldInfo.lenRpn;
            if (lenRpn != null) {
                for (RpnUtil.Ele_int cur : lenRpn) {
                    switch (cur.type) {
                        case 2:
                        case 3: {
                            cur.val = (cur.val - varValArrOffset);
                        }
                    }
                }
            }
            RpnUtil.Ele_int[] listLenRpn = fieldInfo.listLenRpn;
            if (listLenRpn != null) {
                for (RpnUtil.Ele_int cur : listLenRpn) {
                    switch (cur.type) {
                        case 2:
                        case 3: {
                            cur.val = (cur.val - varValArrOffset);
                        }
                    }
                }
            }
        }


        return packetInfo;
    }

    public static int findProcessorIndexByFieldProcessorClass(Class clazz, FieldProcessor[] processors) {
        for (int i = 0; i < processors.length; i++) {
            if (clazz.isAssignableFrom(processors[i].getClass())) {
                return i;
            }
        }
        throw BaseRuntimeException.getException("class[" + clazz.getName() + "] FieldProcessor not exist");
    }

}

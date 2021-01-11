package com.bcd.parser.util;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.info.PacketInfo;
import com.bcd.parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ParserUtil {

    /**
     * 通过扫描包中所有class方式获取所有带{@link Parsable}注解的类
     * @param packageNames
     * @return
     */
    public static List<Class> getParsableClassByScanPackage(String ... packageNames){
        try {
            return ClassUtil.getClassesWithAnno(Parsable.class,packageNames);
        } catch (IOException |ClassNotFoundException e) {
            throw BaseRuntimeException.getException(e);
        }
    }



    /**
     * 解析类转换成包信息
     * @param clazz
     * @param processors
     * @return
     */
    public static PacketInfo toPacketInfo(Class clazz, FieldProcessor[] processors){
        String className=clazz.getName();
        PacketInfo packetInfo=new PacketInfo();
        packetInfo.setClazz(clazz);
        List<Field> allFieldList= FieldUtil.getAllFieldsList(clazz);
        //求出最小var char int和最大var char int
        int[] maxVarInt=new int[1];
        int[] minVarInt=new int[1];
        /**
         * 1、过滤所有带{@link PacketField}的字段
         * 2、将字段按照{@link PacketField#index()}正序
         * 3、将每个字段类型解析成FieldInfo
         */
        List<FieldInfo> fieldInfoList=allFieldList.stream().filter(field -> field.getAnnotation(PacketField.class)!=null).sorted((f1, f2)->{
            int i1=f1.getAnnotation(PacketField.class).index();
            int i2=f2.getAnnotation(PacketField.class).index();
            if(i1<i2){
                return -1;
            }else if(i1>i2){
                return 1;
            }else{
                return 0;
            }
        }).map(field->{
            field.setAccessible(true);
            PacketField packetField= field.getAnnotation(PacketField.class);
            Class fieldType=field.getType();
            Class typeClazz=null;
            boolean isVar=false;
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
            if(packetField.processorClass()==Void.class){
                //判断是否是List<Bean>(Bean代表自定义实体类型,不包括Byte、Short、Integer、Long)
                if(packetField.listLenExpr().isEmpty()) {
                    if (Byte.class.isAssignableFrom(fieldType) || Byte.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex=0;
                    } else if (Short.class.isAssignableFrom(fieldType) || Short.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex=1;
                    } else if (Integer.class.isAssignableFrom(fieldType) || Integer.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex=2;
                    } else if (Long.class.isAssignableFrom(fieldType) || Long.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex=3;
                    } else if (Float.class.isAssignableFrom(fieldType) || Float.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex=4;
                    } else if (Double.class.isAssignableFrom(fieldType) || Double.TYPE.isAssignableFrom(fieldType)) {
                        processorIndex=5;
                    } else if (String.class.isAssignableFrom(fieldType)) {
                        processorIndex=12;
                    } else if (Date.class.isAssignableFrom(fieldType)) {
                        processorIndex=13;
                    } else if (fieldType.isArray()) {
                        //数组类型
                        Class arrType = fieldType.getComponentType();
                        if (Byte.class.isAssignableFrom(arrType) || Byte.TYPE.isAssignableFrom(arrType)) {
                            processorIndex=6;
                        } else if (Short.class.isAssignableFrom(arrType) || Short.TYPE.isAssignableFrom(arrType)) {
                            processorIndex=7;
                        } else if (Integer.class.isAssignableFrom(arrType) || Integer.TYPE.isAssignableFrom(arrType)) {
                            processorIndex=8;
                        } else if (Long.class.isAssignableFrom(arrType) || Long.TYPE.isAssignableFrom(arrType)) {
                            processorIndex=9;
                        } else if (Float.class.isAssignableFrom(arrType) || Float.TYPE.isAssignableFrom(arrType)) {
                            processorIndex=10;
                        } else if (Double.class.isAssignableFrom(arrType) || Double.TYPE.isAssignableFrom(arrType)) {
                            processorIndex=11;
                        } else{
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] Array Type[" + arrType.getName() + "] Not Support");
                        }
                    } else if(ByteBuf.class.isAssignableFrom(fieldType)){
                        //ByteBuf类型
                        processorIndex=14;
                    } else {
                        /**
                         * 带{@link com.bcd.parser.anno.Parsable}注解的实体类
                         */
                        if(fieldType.getAnnotation(Parsable.class)==null){
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] Bean Type[" + fieldType + "] Not Support,Must have annotation [com.bcd.parser.anno.Parsable]");
                        }
                        typeClazz = fieldType;
                        processorIndex=17;
                    }
                }else{
                    if(fieldType.isArray()){
                        typeClazz = fieldType.getComponentType();
                        //检查数组对象类型是否支持解析
                        if(typeClazz.getAnnotation(Parsable.class)!=null){
                            processorIndex=16;
                        }else {
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] Array Type[" + typeClazz.getName() + "] Not Support");
                        }
                    }else {
                        //实体类型集合
                        typeClazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        if(typeClazz.getAnnotation(Parsable.class)!=null){
                            processorIndex = 15;
                        }else {
                            throw BaseRuntimeException.getException("Class[" + clazz.getName() + "] Field[" + field.getName() + "] List Type[" + typeClazz.getName() + "] Not Support");
                        }
                    }
                }
            }else{
                //特殊处理,自定义实体类型
                processorIndex=findProcessorIndexByFieldProcessorClass(packetField.processorClass(),processors);
            }

            //转换逆波兰表达式
            Object[] lenRpn=null;
            Object[] listLenRpn=null;
            Object[] valRpn=null;
            Object[] reserveValRpn=null;
            if(!packetField.lenExpr().isEmpty()){
                lenRpn= RpnUtil.doWithRpnList_char_int(RpnUtil.parseArithmeticToRPN(packetField.lenExpr()));
            }
            if(!packetField.listLenExpr().isEmpty()){
                listLenRpn= RpnUtil.doWithRpnList_char_int(RpnUtil.parseArithmeticToRPN(packetField.listLenExpr()));
            }
            if(!packetField.valExpr().isEmpty()){
                String[] curValRpn=RpnUtil.parseArithmeticToRPN(packetField.valExpr());
                String[] curReserveValRpn=RpnUtil.reverseSimpleRPN(curValRpn);
                valRpn= RpnUtil.doWithRpnList_char_double(curValRpn);
                reserveValRpn= RpnUtil.doWithRpnList_char_double(curReserveValRpn);
            }

            //判断是否变量
            if(packetField.var()!='0'){
                isVar=true;
            }

            //求maxVarInt、minVarInt
            if(lenRpn!=null) {
                for (Object o : lenRpn) {
                    if (o instanceof Character) {
                        if (maxVarInt[0]==0||(char) o > maxVarInt[0]) {
                            maxVarInt[0]=(char) o;
                        }
                        if( minVarInt[0]==0||(char) o < minVarInt[0]){
                            minVarInt[0]=(char) o;
                        }
                    }
                }
            }
            if(listLenRpn!=null) {
                for (Object o : listLenRpn) {
                    if (o instanceof Character) {
                        if (maxVarInt[0]==0||(char) o > maxVarInt[0]) {
                            maxVarInt[0]=(char) o;
                        }
                        if( minVarInt[0]==0||(char) o < minVarInt[0]){
                            minVarInt[0]=(char) o;
                        }
                    }
                }
            }

            FieldInfo fieldInfo=new FieldInfo();
            fieldInfo.setPacketInfo(packetInfo);
            fieldInfo.setField(field);
            fieldInfo.setVar(isVar);
            fieldInfo.setClazz(typeClazz);
            fieldInfo.setProcessorIndex(processorIndex);
            fieldInfo.setLenRpn(lenRpn);
            fieldInfo.setListLenRpn(listLenRpn);
            fieldInfo.setValRpn(valRpn);
            fieldInfo.setValPrecision(packetField.valPrecision());
            fieldInfo.setReverseValRpn(reserveValRpn);
            fieldInfo.setPacketField_index(packetField.index());
            fieldInfo.setPacketField_len(packetField.len());
            fieldInfo.setPacketField_lenExpr(packetField.lenExpr());
            fieldInfo.setPacketField_listLenExpr(packetField.listLenExpr());
            fieldInfo.setPacketField_singleLen(packetField.singleLen());
            fieldInfo.setPacketField_var(packetField.var());
            fieldInfo.setPacketField_var_int(packetField.var());
            fieldInfo.setPacketField_parserClass(packetField.processorClass());
            fieldInfo.setPacketField_valExpr(packetField.valExpr());
            return fieldInfo;
        }).collect(Collectors.toList());
        packetInfo.setFieldInfos(fieldInfoList.toArray(new FieldInfo[0]));

        if(maxVarInt[0]!=0){
            packetInfo.setVarValArrLen(maxVarInt[0]-minVarInt[0]+1);
            packetInfo.setVarValArrOffset(minVarInt[0]);
        }

        return packetInfo;
    }

    public static int findProcessorIndexByFieldProcessorClass(Class clazz,FieldProcessor[] processors){
        for (int i=0;i<processors.length;i++) {
            if(clazz==processors[i].getClass()){
                return i;
            }
        }
        throw BaseRuntimeException.getException("class["+clazz.getName()+"] FieldProcessor not exist");
    }
}

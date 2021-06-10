package com.bcd.parser.util;

import com.bcd.parser.exception.BaseRuntimeException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnsafeUtil {
    public final static Unsafe unsafe = getUnsafe();

    public static long fieldOffset(Field field){
        if(Modifier.isStatic(field.getModifiers())){
            return unsafe.staticFieldOffset(field);
        }else{
            return unsafe.objectFieldOffset(field);
        }
    }

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw BaseRuntimeException.getException(e);
        }
    }

    public static int fieldType(Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType == byte.class) {
            return 1;
        } else if (fieldType == short.class) {
            return 2;
        } else if (fieldType == int.class) {
            return 3;
        } else if (fieldType == long.class) {
            return 4;
        } else if (fieldType == float.class) {
            return 5;
        } else if (fieldType == double.class) {
            return 6;
        } else if (fieldType == char.class) {
            return 7;
        } else if (fieldType == boolean.class) {
            return 8;
        } else {
            return 0;
        }
    }

    /**
     * unsafe方法获取值
     *
     * @param instance
     * @param offset
     * @param type
     * @return
     */
    public static Object getValue(Object instance, long offset, int type) {
        switch (type) {
            case 1: {
                return unsafe.getByte(instance, offset);
            }
            case 2: {
                return unsafe.getShort(instance, offset);
            }
            case 3: {
                return unsafe.getInt(instance, offset);
            }
            case 4: {
                return unsafe.getLong(instance, offset);
            }
            case 5: {
                return unsafe.getFloat(instance, offset);
            }
            case 6: {
                return unsafe.getDouble(instance, offset);
            }
            case 7: {
                return unsafe.getChar(instance, offset);
            }
            case 8: {
                return unsafe.getBoolean(instance, offset);
            }
            default: {
                return unsafe.getObject(instance, offset);
            }
        }
    }

    /**
     * unsafe设置值、替代反射
     *
     * @param instance
     * @param val
     * @param offset
     * @param type
     */
    public static void setValue(Object instance, Object val, long offset, int type) {
        switch (type) {
            case 1: {
                unsafe.putByte(instance, offset, (byte) val);
                break;
            }
            case 2: {
                unsafe.putShort(instance, offset, (short) val);
                break;
            }
            case 3: {
                unsafe.putInt(instance, offset, (int) val);
                break;
            }
            case 4: {
                unsafe.putLong(instance, offset, (long) val);
                break;
            }
            case 5: {
                unsafe.putFloat(instance, offset, (float) val);
                break;
            }
            case 6: {
                unsafe.putDouble(instance, offset, (double) val);
                break;
            }
            case 7: {
                unsafe.putChar(instance, offset, (char) val);
                break;
            }
            case 8: {
                unsafe.putBoolean(instance, offset, (boolean) val);
                break;
            }
            default: {
                unsafe.putObject(instance, offset, val);
            }
        }

    }
}

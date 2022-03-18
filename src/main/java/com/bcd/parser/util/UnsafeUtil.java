package com.bcd.parser.util;

import com.bcd.parser.exception.BaseRuntimeException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnsafeUtil {

    public static Unsafe getUnsafe(){
        return Singleton.INSTANCE;
    }

    private final static class Singleton{
        private final static Unsafe INSTANCE = getInstance();
        private static Unsafe getInstance() {
            try {
                final Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                return (Unsafe) field.get(null);
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                throw BaseRuntimeException.getException(e);
            }
        }
    }


    public static long fieldOffset(final Field field){
        if(Modifier.isStatic(field.getModifiers())){
            return getUnsafe().staticFieldOffset(field);
        }else{
            return getUnsafe().objectFieldOffset(field);
        }
    }



    public static int fieldType(final Field field) {
        final Class<?> fieldType = field.getType();
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
    public static Object getValue(final Object instance, final long offset, final int type) {
        switch (type) {
            case 1 -> {
                return getUnsafe().getByte(instance, offset);
            }
            case 2 -> {
                return getUnsafe().getShort(instance, offset);
            }
            case 3 -> {
                return getUnsafe().getInt(instance, offset);
            }
            case 4 -> {
                return getUnsafe().getLong(instance, offset);
            }
            case 5 -> {
                return getUnsafe().getFloat(instance, offset);
            }
            case 6 -> {
                return getUnsafe().getDouble(instance, offset);
            }
            case 7 -> {
                return getUnsafe().getChar(instance, offset);
            }
            case 8 -> {
                return getUnsafe().getBoolean(instance, offset);
            }
            default -> {
                return getUnsafe().getObject(instance, offset);
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
    public static void setValue(final Object instance, final Object val, final long offset, final int type) {
        switch (type) {
            case 1 -> {
                getUnsafe().putByte(instance, offset, (byte) val);
            }
            case 2 -> {
                getUnsafe().putShort(instance, offset, (short) val);
            }
            case 3 -> {
                getUnsafe().putInt(instance, offset, (int) val);
            }
            case 4 -> {
                getUnsafe().putLong(instance, offset, (long) val);
            }
            case 5 -> {
                getUnsafe().putFloat(instance, offset, (float) val);
            }
            case 6 -> {
                getUnsafe().putDouble(instance, offset, (double) val);
            }
            case 7 -> {
                getUnsafe().putChar(instance, offset, (char) val);
            }
            case 8 -> {
                getUnsafe().putBoolean(instance, offset, (boolean) val);
            }
            default -> {
                getUnsafe().putObject(instance, offset, val);
            }
        }

    }
}

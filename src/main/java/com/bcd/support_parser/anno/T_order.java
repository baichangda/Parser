package com.bcd.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 用于指定在类上、来约束大小端模式、约束范围为
 * 此类下所有字段、此类下所有bean类下所有字段(递归)
 * 包含如下注解、中order属性
 * {@link F_float_ieee754#order()}
 * {@link F_float_ieee754_array#order()}
 * {@link F_float_integer#order()}
 * {@link F_float_integer_array#order()}
 * {@link F_integer#order()}
 * {@link F_integer_array#order()}
 * {@link F_date#order()}
 *
 * 注意:
 * 优先级为
 * 1、字段注解{@link ByteOrder}!={@link ByteOrder#Default}
 * 2、类或上级类注解{@link T_order#order()}!={@link ByteOrder#Default}、注意如果存在多个{@link T_order}、作用域更小的替换大的
 * 3、字段注解{@link ByteOrder#Default}、此时值即是{@link ByteOrder#BigEndian}
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface T_order {
    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.Default;
}

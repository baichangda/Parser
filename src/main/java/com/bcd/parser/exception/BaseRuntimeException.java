package com.bcd.parser.exception;


import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 建造此异常类的目的:
 * 1、在所有需要抛非运行时异常的地方,用此异常包装,避免方法调用时候需要捕获异常(若是其他框架自定义的异常,请不要用此类包装)
 * 2、在业务需要出异常的时候,定义异常并且抛出
 */
public class BaseRuntimeException extends RuntimeException {
    private BaseRuntimeException(String message) {
        super(message);
    }

    private BaseRuntimeException(Throwable e) {
        super(e);
    }

    public static BaseRuntimeException getException(String message) {
        return new BaseRuntimeException(message);
    }

    /**
     * 将异常信息转换为格式化
     * @param message
     * @param params
     * @return
     */
    public static BaseRuntimeException getException(String message, Object ... params){
        Object[] newParams=Arrays.stream(params).map(e->e==null?"":e.toString()).toArray();
        return new BaseRuntimeException(MessageFormat.format(message,newParams));
    }

    public static BaseRuntimeException getException(Throwable e) {
        return new BaseRuntimeException(e);
    }

    public static void main(String[] args) {
        throw BaseRuntimeException.getException("[{0}]-[{1}]",null,100000);
    }
}

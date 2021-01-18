package com.bcd.parser.exception;


import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 建造此异常类的目的:
 * 1、在所有需要抛非运行时异常的地方,用此异常包装,避免方法调用时候需要捕获异常(若是其他框架自定义的异常,请不要用此类包装)
 * 2、在业务需要出异常的时候,定义异常并且抛出
 */
public class BaseRuntimeException extends RuntimeException {
    protected String code;

    public String getCode() {
        return code;
    }

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
     * val表达式从{0}开始
     * @param message 注意其中特殊字符为'、如果要显示特殊字符使用''
     * @param params
     * @return
     */
    public static BaseRuntimeException getException(String message, Object ... params){
        return new BaseRuntimeException(MessageFormat.format(
                //转义特殊字符'为''
//                message.replaceAll("'","''")
                message
                //去除null
                ,Arrays.stream(params).map(e->e==null?"":e.toString()).toArray())
        );
    }

    public static BaseRuntimeException getException(Throwable e) {
        return new BaseRuntimeException(e);
    }

    public static BaseRuntimeException getException(Throwable e, String code) {
        return new BaseRuntimeException(e).withCode(code);
    }

    public BaseRuntimeException withCode(String code) {
        this.code = code;
        return this;
    }

    public static void main(String[] args) {
        throw BaseRuntimeException.getException("[''{0}]-[{1}]",null,100000);
    }
}

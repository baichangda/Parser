package com.bcd.parser.processer;


import com.bcd.parser.Parser;
import com.bcd.parser.exception.BaseRuntimeException;
import com.bcd.parser.info.FieldInfo;
import com.bcd.parser.info.PacketInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FieldProcessor<T> {
    public Parser parser;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public T process_print(ByteBuf data, FieldProcessContext processContext) {
        int startIndex = data.readerIndex();
        T val = process(data, processContext);
        int endIndex = data.readerIndex();
        byte[] bytesVal = new byte[endIndex - startIndex];
        data.getBytes(startIndex, bytesVal);
        print(processContext, bytesVal, val);
        return val;
    }

    public void deProcess_print(T data, ByteBuf dest, FieldDeProcessContext processContext) {
        int startIndex = dest.writerIndex();
        deProcess(data, dest, processContext);
        int endIndex = dest.writerIndex();
        byte[] bytesVal = new byte[endIndex - startIndex];
        dest.getBytes(startIndex, bytesVal);
        print(processContext, bytesVal, data);
    }

    protected void print(FieldProcessContext processContext, byte[] bytesVal, Object val) {
        PacketInfo packetInfo = processContext.fieldInfo.packetInfo;
        FieldInfo fieldInfo = processContext.fieldInfo;
        if (fieldInfo.packetField_valExpr.isEmpty()) {
            logger.info("parse class[{}] field[{}] hex[{}] val[{}] parser[{}]",
                    packetInfo.clazz.getName(),
                    fieldInfo.field.getName(),
                    ByteBufUtil.hexDump(bytesVal),
                    val,
                    this.getClass().getName());
        } else {
            logger.info("parse class[{}] field[{}] hex[{}] val[{}] valExpr[{}] parser[{}]",
                    packetInfo.clazz.getName(),
                    fieldInfo.field.getName(),
                    ByteBufUtil.hexDump(bytesVal),
                    val,
                    fieldInfo.packetField_valExpr,
                    this.getClass().getName());
        }
    }

    protected void print(FieldDeProcessContext processContext, byte[] bytesVal, Object val) {
        PacketInfo packetInfo = processContext.fieldInfo.packetInfo;
        FieldInfo fieldInfo = processContext.fieldInfo;
        if (fieldInfo.packetField_valExpr.isEmpty()) {
            logger.info("deParse class[{}] field[{}] hex[{}] val[{}] parser[{}]",
                    packetInfo.clazz.getName(),
                    fieldInfo.field.getName(),
                    ByteBufUtil.hexDump(bytesVal),
                    val,
                    this.getClass().getName());
        } else {
            logger.info("deParse class[{}] field[{}] hex[{}] val[{}] valExpr[{}] parser[{}]",
                    packetInfo.clazz.getName(),
                    fieldInfo.field.getName(),
                    ByteBufUtil.hexDump(bytesVal),
                    val,
                    fieldInfo.packetField_valExpr,
                    this.getClass().getName());
        }
    }

    /**
     * 读取byteBuf数据转换成对象
     *
     * @param data
     * @param processContext
     * @return
     */
    public T process(ByteBuf data, FieldProcessContext processContext) {
        throw BaseRuntimeException.getException("process not support");
    }

    /**
     * 解析对象转换为byteBuf
     *
     * @param data
     * @param dest
     * @param processContext
     */
    public void deProcess(T data, ByteBuf dest, FieldDeProcessContext processContext) {
        throw BaseRuntimeException.getException("deProcess not support");
    }

}

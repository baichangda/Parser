package com.bcd.support_parser.impl.someip.processor;

import com.bcd.support_parser.impl.someip.data.Packet;
import com.bcd.support_parser.info.FieldInfo;
import com.bcd.support_parser.info.PacketInfo;
import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class MethodIdOrEventIdProcessor extends FieldProcessor<Short> {

    @Override
    public Short process(ByteBuf data, FieldProcessContext processContext) {
        short s = data.readShort();
        //解析flag
        ((Packet) processContext.instance).flag=(byte) ((s >> 7) & 0b01);
        return (short) (s & (0xff >> 1));
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        dest.writeShort(
                (((Packet) processContext.instance).flag << 7) | data
        );
    }

    @Override
    protected void print(FieldProcessContext processContext, byte[] bytesVal, Object val) {
        PacketInfo packetInfo = processContext.fieldInfo.packetInfo;
        FieldInfo fieldInfo = processContext.fieldInfo;
        logger.info("parse class[{}] field[{},{}] hex[{}] val[{},{}] parser[{}]",
                packetInfo.clazz.getName(),
                "flag",
                fieldInfo.field.getName(),
                ByteBufUtil.hexDump(bytesVal),
                ((Packet) processContext.instance).flag,
                val,
                this.getClass().getName());
    }

    @Override
    protected void print(FieldDeProcessContext processContext, byte[] bytesVal, Object val) {
        PacketInfo packetInfo = processContext.fieldInfo.packetInfo;
        FieldInfo fieldInfo = processContext.fieldInfo;
        logger.info("deParse class[{}] field[{},{}] hex[{}] val[{},{}] parser[{}]",
                packetInfo.clazz.getName(),
                "flag",
                fieldInfo.field.getName(),
                ByteBufUtil.hexDump(bytesVal),
                ((Packet) processContext.instance).flag,
                val,
                this.getClass().getName());
    }
}

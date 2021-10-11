package com.bcd.parser.impl.someip.reflect.processor;

import com.bcd.parser.impl.someip.data.Packet;
import com.bcd.parser.reflect.info.FieldInfo;
import com.bcd.parser.reflect.info.PacketInfo;
import com.bcd.parser.reflect.processer.FieldDeProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessContext;
import com.bcd.parser.reflect.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class MethodIdOrEventIdProcessor extends FieldProcessor<Short> {

    @Override
    public Short process(ByteBuf data, FieldProcessContext processContext) {
        short s = data.readShort();
        //解析flag
        ((Packet) processContext.instance).setFlag((byte) ((s >> 7) & 0b01));
        return (short) (s & (0xff >> 1));
    }

    @Override
    public void deProcess(Short data, ByteBuf dest, FieldDeProcessContext processContext) {
        dest.writeShort(
                (((Packet) processContext.instance).getFlag() << 7) | data
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
                ((Packet) processContext.instance).getFlag(),
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
                ((Packet) processContext.instance).getFlag(),
                val,
                this.getClass().getName());
    }
}

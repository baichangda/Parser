package com.bcd.parser.impl.someip.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import com.bcd.parser.impl.someip.javassist.processor.MessageTypeProcessor;
import com.bcd.parser.impl.someip.javassist.processor.MethodIdOrEventIdProcessor;
import com.bcd.parser.impl.someip.javassist.processor.OffsetProcessor;
import com.bcd.parser.impl.someip.javassist.processor.ReturnCodeProcessor;

@Parsable
public class Packet {
    @PacketField(index = 1, len = 2)
    int serviceId;

    /**
     * 与{@link #methodIdOrEventId} 一起处理
     */
    byte flag;

    @PacketField(index = 3, processorClass = MethodIdOrEventIdProcessor.class)
    short methodIdOrEventId;

    @PacketField(index = 4, len = 4)
    long length;

    @PacketField(index = 5, len = 2)
    int clientId;

    @PacketField(index = 6, len = 2)
    int sessionId;

    @PacketField(index = 7, len = 1)
    short protocolVersion;

    @PacketField(index = 8, len = 1)
    short interfaceVersion;

    @PacketField(index = 9, len = 1, processorClass = MessageTypeProcessor.class)
    MessageType messageType;

    @PacketField(index = 10, len = 1, processorClass = ReturnCodeProcessor.class)
    ReturnCode returnCode;

    @PacketField(index = 11,len = 4,processorClass = OffsetProcessor.class,var = 'a')
    int offset;

    @PacketField(index = 12,lenExpr = "a*16")
    byte[] payload;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public short getMethodIdOrEventId() {
        return methodIdOrEventId;
    }

    public void setMethodIdOrEventId(short methodIdOrEventId) {
        this.methodIdOrEventId = methodIdOrEventId;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public short getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(short protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public short getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(short interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}

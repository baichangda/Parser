package com.bcd.support_parser.impl.someip.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;
import com.bcd.support_parser.impl.someip.processor.MessageTypeProcessor;
import com.bcd.support_parser.impl.someip.processor.MethodIdOrEventIdProcessor;
import com.bcd.support_parser.impl.someip.processor.OffsetProcessor;
import com.bcd.support_parser.impl.someip.processor.ReturnCodeProcessor;

@Parsable
public class Packet {
    @PacketField(index = 1, len = 2)
    public int serviceId;

    /**
     * 与{@link #methodIdOrEventId} 一起处理
     */
    public byte flag;

    @PacketField(index = 3, processorClass = MethodIdOrEventIdProcessor.class)
    public short methodIdOrEventId;

    @PacketField(index = 4, len = 4)
    public long length;

    @PacketField(index = 5, len = 2)
    public int clientId;

    @PacketField(index = 6, len = 2)
    public int sessionId;

    @PacketField(index = 7, len = 1)
    public short protocolVersion;

    @PacketField(index = 8, len = 1)
    public short interfaceVersion;

    @PacketField(index = 9, len = 1, processorClass = MessageTypeProcessor.class)
    public MessageType messageType;

    @PacketField(index = 10, len = 1, processorClass = ReturnCodeProcessor.class)
    public ReturnCode returnCode;

    @PacketField(index = 11,len = 4,processorClass = OffsetProcessor.class,var = 'a')
    public int offset;

    @PacketField(index = 10,lenExpr = "a*16")
    public byte[] payload;
}

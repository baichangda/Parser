package com.bcd.support_parser.impl.someip.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.impl.someip.javassist.builder.MessageTypeBuilder;
import com.bcd.support_parser.impl.someip.javassist.builder.MethodIdOrEventIdBuilder;
import com.bcd.support_parser.impl.someip.javassist.builder.OffsetBuilder;
import com.bcd.support_parser.impl.someip.javassist.builder.ReturnCodeBuilder;

public class Packet {
    @PacketField(index = 1, len = 2)
    public int serviceId;

    /**
     * 与{@link #methodIdOrEventId} 一起处理
     */
    public byte flag;

    @PacketField(index = 3
//            , processorClass = MethodIdOrEventIdProcessor.class
            , builderClass = MethodIdOrEventIdBuilder.class
    )
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

    @PacketField(index = 9, len = 1
//            , processorClass = MessageTypeProcessor.class
            , builderClass = MessageTypeBuilder.class
    )
    public MessageType messageType;

    @PacketField(index = 10, len = 1
//            , processorClass = ReturnCodeProcessor.class
            , builderClass = ReturnCodeBuilder.class
    )
    public ReturnCode returnCode;

    @PacketField(index = 11, len = 4,
//            processorClass = OffsetProcessor.class,
            builderClass = OffsetBuilder.class,
            var = 'a'
    )
    public int offset;

    @PacketField(index = 12, lenExpr = "a*16")
    public byte[] payload;
}

package com.bcd.support_parser.impl.someip.data;

import com.bcd.support_parser.anno.*;
import com.bcd.support_parser.impl.someip.builder.MessageTypeBuilder;
import com.bcd.support_parser.impl.someip.builder.OffsetBuilder;
import com.bcd.support_parser.impl.someip.builder.ReturnCodeBuilder;

public class Packet {
    @F_integer(len = 2)
    public int serviceId;

    @F_integer(bit = 1)
    public byte flag;

    @F_integer(bit = 7)
    public byte methodIdOrEventId;

    @F_integer(len = 4)
    public long length;

    @F_integer(len = 2)
    public int clientId;

    @F_integer(len = 2)
    public int sessionId;

    @F_integer(len = 1)
    public short protocolVersion;

    @F_integer(len = 1)
    public short interfaceVersion;

    @F_userDefine(
//            , processorClass = MessageTypeProcessor.class
            builderClass = MessageTypeBuilder.class
    )
    public MessageType messageType;

    @F_userDefine(
//            , processorClass = ReturnCodeProcessor.class
            builderClass = ReturnCodeBuilder.class
    )
    public ReturnCode returnCode;

    @F_userDefine(
//            processorClass = OffsetProcessor.class,
            builderClass = OffsetBuilder.class,
            var = 'a'
    )
    public int offset;

    @F_integer_array(lenExpr = "a*16",singleLen = 1)
    public byte[] payload;
}

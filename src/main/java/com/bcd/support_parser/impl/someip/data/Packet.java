package com.bcd.support_parser.impl.someip.data;

import com.bcd.support_parser.anno.*;

public class Packet {
    @F_integer(len = 2)
    public int serviceId;

    @F_integer(bit = 1)
    public byte flag;

    @F_integer(bit = 15)
    public short methodIdOrEventId;

    @F_integer(len = 4,var = 'a')
    public long length;

    @F_integer(len = 2)
    public int clientId;

    @F_integer(len = 2)
    public int sessionId;

    @F_integer(len = 1)
    public short protocolVersion;

    @F_integer(len = 1)
    public short interfaceVersion;

    @F_integer(len = 1)
    public MessageType messageType;

    @F_integer(len = 1)
    public ReturnCode returnCode;

    @F_integer_array(lenExpr = "a-8",singleLen = 1)
    public byte[] payload;
}

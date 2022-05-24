package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

import java.util.Date;

@Parsable
public class PlatformLogoutData implements PacketData{
    //登出时间
    @PacketField(index = 1,len = 6)
    public Date collectTime;

    //登出流水号
    @PacketField(index = 2,len = 2)
    public int sn;
}

package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_date;
import com.bcd.support_parser.anno.F_integer;

import java.util.Date;

public class PlatformLogoutData implements PacketData{
    //登出时间
    @F_date
    public Date collectTime;

    //登出流水号
    @F_integer(len = 2)
    public int sn;
}

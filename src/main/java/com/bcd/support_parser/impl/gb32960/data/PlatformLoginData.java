package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;

import java.util.Date;

public class PlatformLoginData implements PacketData {
    //平台登入时间
    @PacketField(index = 1,len = 6)
    public Date collectTime;

    //登入流水号
    @PacketField(index = 2,len = 2)
    public int sn;

    //平台用户名
    @PacketField(index = 3,len = 12)
    public String username;

    //平台密码
    @PacketField(index = 4,len = 20)
    public String password;

    //加密规则
    @PacketField(index = 5,len = 1)
    public short encode;
}

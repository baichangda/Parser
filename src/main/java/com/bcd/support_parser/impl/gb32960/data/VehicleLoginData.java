package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

import java.util.Date;

@Parsable
public class VehicleLoginData implements PacketData{
    //数据采集时间
    @PacketField(index = 1,len = 6)
    public Date collectTime;

    //登入流水号
    @PacketField(index = 2,len = 2)
    public int sn;

    //iccid
    @PacketField(index = 3,len = 20)
    public String iccid;

    //可充电储能子系统数
    @PacketField(index = 4,len = 1,var = 'n')
    public short subSystemNum;

    //可充电储能系统编码长度
    @PacketField(index = 5,len = 1,var = 'm')
    public byte systemCodeLen;

    //可充电储能系统编码
    @PacketField(index = 6,lenExpr = "n*m")
    public String systemCode;
}

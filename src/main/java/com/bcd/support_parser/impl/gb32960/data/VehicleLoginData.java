package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_date;
import com.bcd.support_parser.anno.F_integer;
import com.bcd.support_parser.anno.F_string;

import java.util.Date;

public class VehicleLoginData implements PacketData{
    //数据采集时间
    @F_date
    public Date collectTime;

    //登入流水号
    @F_integer(len = 2)
    public int sn;

    //iccid
    @F_string(len = 20)
    public String iccid;

    //可充电储能子系统数
    @F_integer(len = 1,var = 'n')
    public short subSystemNum;

    //可充电储能系统编码长度
    @F_integer(len = 1,var = 'm')
    public byte systemCodeLen;

    //可充电储能系统编码
    @F_string(lenExpr = "n*m")
    public String systemCode;
}

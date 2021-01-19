package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

import java.util.Date;

@Parsable
public class VehicleLoginData implements PacketData{
    //数据采集时间
    @PacketField(index = 1,len = 6)
    Date collectTime;

    //登入流水号
    @PacketField(index = 2,len = 2)
    int sn;

    //iccid
    @PacketField(index = 3,len = 20)
    String iccid;

    //可充电储能子系统数
    @PacketField(index = 4,len = 1,var = 'n')
    short subSystemNum;

    //可充电储能系统编码长度
    @PacketField(index = 5,len = 1,var = 'm')
    byte systemCodeLen;

    //可充电储能系统编码
    @PacketField(index = 6,lenExpr = "n*m")
    String systemCode;

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }


    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public short getSubSystemNum() {
        return subSystemNum;
    }

    public void setSubSystemNum(short subSystemNum) {
        this.subSystemNum = subSystemNum;
    }

    public byte getSystemCodeLen() {
        return systemCodeLen;
    }

    public void setSystemCodeLen(byte systemCodeLen) {
        this.systemCodeLen = systemCodeLen;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}

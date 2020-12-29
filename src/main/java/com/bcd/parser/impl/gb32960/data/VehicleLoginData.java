package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Parsable
public class VehicleLoginData extends PacketData{
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

}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

import java.util.Date;

@Parsable
public class VehicleLogoutData implements PacketData{
    //登出时间
    @PacketField(index = 1,len = 6)
    Date collectTime;

    //登出流水号
    @PacketField(index = 2,len = 2)
    int sn;

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
}

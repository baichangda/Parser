package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

import java.util.ArrayList;
import java.util.List;

/**
 * 可充电储能装置电压数据
 */
@Parsable
public class VehicleStorageVoltageData {
    //可充电储能子系统个数
    @PacketField(index = 1,len = 1,var = 'a')
    short num;

    //可充电储能子系统电压信息集合
    @PacketField(index = 2,listLenExpr = "a")
    List<StorageVoltageData> content;


    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public List<StorageVoltageData> getContent() {
        return content;
    }

    public void setContent(List<StorageVoltageData> content) {
        this.content = content;
    }
}

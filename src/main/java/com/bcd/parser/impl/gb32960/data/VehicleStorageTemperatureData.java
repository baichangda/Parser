package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

import java.util.ArrayList;
import java.util.List;

/**
 * 可充电储能装置温度数据
 */
@Parsable
public class VehicleStorageTemperatureData {
    //可充电储能子系统个数
    @PacketField(index = 1,len = 1,var = 'a')
    short num;

    //可充电储能子系统温度信息列表
    @PacketField(index = 2,listLenExpr = "a")
    List<StorageTemperatureData> content;


    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public List<StorageTemperatureData> getContent() {
        return content;
    }

    public void setContent(List<StorageTemperatureData> content) {
        this.content = content;
    }
}

package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

import java.util.List;

/**
 * 可充电储能装置温度数据
 */
@Parsable
public class VehicleStorageTemperatureData {
    //可充电储能子系统个数
    @PacketField(index = 1,len = 1,var = 'a')
    public short num;

    //可充电储能子系统温度信息列表
    @PacketField(index = 2,listLenExpr = "a")
    public List<StorageTemperatureData> content;
}

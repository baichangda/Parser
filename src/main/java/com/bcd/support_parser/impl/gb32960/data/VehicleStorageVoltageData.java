package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;

import java.util.List;

/**
 * 可充电储能装置电压数据
 */
public class VehicleStorageVoltageData {
    //可充电储能子系统个数
    @PacketField(index = 1,len = 1,var = 'a')
    public short num;

    //可充电储能子系统电压信息集合
    @PacketField(index = 2,listLenExpr = "a")
    public List<StorageVoltageData> content;
}

package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_bean_list;
import com.bcd.support_parser.anno.F_integer;

import java.util.List;

/**
 * 可充电储能装置温度数据
 */
public class VehicleStorageTemperatureData {
    //可充电储能子系统个数
    @F_integer(len = 1, var = 'a')
    public short num;

    //可充电储能子系统温度信息列表
    @F_bean_list(listLenExpr = "a")
    public List<StorageTemperatureData> content;
}

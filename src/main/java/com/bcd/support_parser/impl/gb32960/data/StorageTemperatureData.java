package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;

/**
 * 每个可充电储能子系统上温度数据格式
 */
public class StorageTemperatureData {
    //可充电储能子系统号
    @PacketField(index = 1,len = 1)
    public short no;

    //可充电储能温度探针个数
    @PacketField(index = 2,len = 2,var = 'n')
    public int num;

    //可充电储能子系统各温度探针检测到的温度值
    @PacketField(index = 3,lenExpr = "n",valExpr = "x-40",singleLen = 1)
    public short[] temperatures;
}

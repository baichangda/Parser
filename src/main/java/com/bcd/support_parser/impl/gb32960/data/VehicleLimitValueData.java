package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 极值数据
 */
@Parsable
public class VehicleLimitValueData {
    //最高电压电池子系统号
    @PacketField(index = 1,len = 1)
    public short maxVoltageSystemNo;

    //最高电压电池单体代号
    @PacketField(index = 2,len = 1)
    public short maxVoltageCode;

    //电池单体电压最高值
    @PacketField(index = 3,len = 2,valExpr = "x/1000",valPrecision = 3)
    public float maxVoltage;

    //最低电压电池子系统号
    @PacketField(index = 4,len = 1)
    public short minVoltageSystemNo;

    //最低电压电池单体代号
    @PacketField(index = 5,len = 1)
    public short minVoltageCode;

    //电池单体电压最低值
    @PacketField(index = 6,len = 2,valExpr = "x/1000",valPrecision = 3)
    public float minVoltage;

    //最高温度子系统号
    @PacketField(index = 7,len = 1)
    public short maxTemperatureSystemNo;

    //最高温度探针序号
    @PacketField(index = 8,len = 1)
    public short maxTemperatureNo;

    //最高温度值
    @PacketField(index = 9,len = 1,valExpr = "x-40")
    public short maxTemperature;

    //最低温度子系统号
    @PacketField(index = 10,len = 1)
    public short minTemperatureSystemNo;

    //最低温度探针序号
    @PacketField(index = 11,len = 1)
    public short minTemperatureNo;

    //最低温度值
    @PacketField(index = 12,len = 1,valExpr = "x-40")
    public short minTemperature;
}

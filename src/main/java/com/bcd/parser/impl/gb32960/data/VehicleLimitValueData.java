package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 极值数据
 */
@Getter
@Setter
@Parsable
public class VehicleLimitValueData {
    //最高电压电池子系统号
    @PacketField(index = 1,len = 1)
    short maxVoltageSystemNo;

    //最高电压电池单体代号
    @PacketField(index = 2,len = 1)
    short maxVoltageCode;

    //电池单体电压最高值
    @PacketField(index = 3,len = 2)
    int maxVoltage;

    //最低电压电池子系统号
    @PacketField(index = 4,len = 1)
    short minVoltageSystemNo;

    //最低电压电池单体代号
    @PacketField(index = 5,len = 1)
    short minVoltageCode;

    //电池单体电压最低值
    @PacketField(index = 6,len = 2)
    int minVoltage;

    //最高温度子系统号
    @PacketField(index = 7,len = 1)
    short maxTemperatureSystemNo;

    //最高温度探针序号
    @PacketField(index = 8,len = 1)
    short maxTemperatureNo;

    //最高温度值
    @PacketField(index = 9,len = 1)
    short maxTemperature;

    //最低温度子系统号
    @PacketField(index = 10,len = 1)
    short minTemperatureSystemNo;

    //最低温度探针序号
    @PacketField(index = 11,len = 1)
    short minTemperatureNo;

    //最低温度值
    @PacketField(index = 12,len = 1)
    short minTemperature;

}

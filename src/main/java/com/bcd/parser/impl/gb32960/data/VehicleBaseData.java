package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 整车数据
 */
@Getter
@Setter
@Parsable
public class VehicleBaseData {
    //车辆状态
    @PacketField(index = 1,len=1)
    short vehicleStatus;

    //充电状态
    @PacketField(index = 2,len=1)
    short chargeStatus;

    //运行模式
    @PacketField(index = 3,len=1)
    short runMode;

    //车速
    @PacketField(index = 4,len=2)
    int vehicleSpeed;

    //累计里程
    @PacketField(index = 5,len=4)
    int totalMileage;

    //总电压
    @PacketField(index = 6,len=2)
    int totalVoltage;

    //总电流
    @PacketField(index = 7,len=2)
    int totalCurrent;

    //soc
    @PacketField(index = 8,len=1)
    short soc;

    //DC-DC状态
    @PacketField(index = 9,len=1)
    short dcStatus;

    //档位
    @PacketField(index = 9,len=1)
    int gearPosition;

    //绝缘电阻
    @PacketField(index = 10,len=2)
    int resistance;

    //加速踏板行程值
    @PacketField(index = 11,len=1)
    short pedalVal;

    //制动踏板状态
    @PacketField(index = 12,len=1)
    short pedalStatus;


}

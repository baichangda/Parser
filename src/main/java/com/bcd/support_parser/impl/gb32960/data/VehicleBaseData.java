package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 整车数据
 */
@Parsable
public class VehicleBaseData {
    //车辆状态
    @PacketField(index = 1, len = 1)
    public short vehicleStatus;

    //充电状态
    @PacketField(index = 2, len = 1)
    public short chargeStatus;

    //运行模式
    @PacketField(index = 3, len = 1)
    public short runMode;

    //车速
    @PacketField(index = 4, len = 2, valExpr = "x/10",valPrecision = 1)
    public float vehicleSpeed;

    //累计里程
    @PacketField(index = 5, len = 4, valExpr = "x/10",valPrecision = 1)
    public double totalMileage;

    //总电压
    @PacketField(index = 6, len = 2, valExpr = "x/10",valPrecision = 1)
    public float totalVoltage;

    //总电流
    @PacketField(index = 7, len = 2, valExpr = "(x-10000)/10",valPrecision = 1)
    public float totalCurrent;

    //soc
    @PacketField(index = 8, len = 1)
    public short soc;

    //DC-DC状态
    @PacketField(index = 9, len = 1)
    public short dcStatus;

    //档位
    @PacketField(index = 9, len = 1)
    public short gearPosition;

    //绝缘电阻
    @PacketField(index = 10, len = 2)
    public int resistance;

    //加速踏板行程值
    @PacketField(index = 11, len = 1)
    public short pedalVal;

    //制动踏板状态
    @PacketField(index = 12, len = 1)
    public short pedalStatus;

    public static void main(String[] args) {
        System.out.println(381.3f*10);
    }
}

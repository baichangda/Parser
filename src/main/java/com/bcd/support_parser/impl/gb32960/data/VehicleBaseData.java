package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_float_integer;
import com.bcd.support_parser.anno.F_integer;

/**
 * 整车数据
 */
public class VehicleBaseData {
    //车辆状态
    @F_integer(len = 1)
    public short vehicleStatus;

    //充电状态
    @F_integer(len = 1)
    public short chargeStatus;

    //运行模式
    @F_integer(len = 1)
    public short runMode;

    //车速
    @F_float_integer(len = 2, valExpr = "x/10")
    public float vehicleSpeed;

    //累计里程
    @F_float_integer(len = 4, valExpr = "x/10")
    public double totalMileage;

    //总电压
    @F_float_integer(len = 2, valExpr = "x/10")
    public float totalVoltage;

    //总电流
    @F_float_integer(len = 2, valExpr = "x/10-1000")
    public float totalCurrent;

    //soc
    @F_integer(len = 1)
    public short soc;

    //DC-DC状态
    @F_integer(len = 1)
    public short dcStatus;

    //档位
    @F_integer(len = 1)
    public short gearPosition;

    //绝缘电阻
    @F_integer(len = 2)
    public int resistance;

    //加速踏板行程值
    @F_integer(len = 1)
    public short pedalVal;

    //制动踏板状态
    @F_integer(len = 1)
    public short pedalStatus;
}

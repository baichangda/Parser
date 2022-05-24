package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;

/**
 * 燃料电池数据
 */
public class VehicleFuelBatteryData {
    //燃料电池电压
    @PacketField(index = 1,len = 2,valExpr = "x/10")
    public float voltage;

    //燃料电池电流
    @PacketField(index = 2,len = 2,valExpr = "x/10")
    public float current;

    //燃料消耗率
    @PacketField(index = 3,len = 2,valExpr = "x/100")
    public float consumptionRate;

    //燃料电池温度探针总数
    @PacketField(index = 4,len = 2,var = 'a')
    public int num;

    //探针温度值
    @PacketField(index =5,lenExpr = "a",valExpr = "x-40",singleLen = 1)
    public short[] temperatures;

    //氢系统中最高温度
    @PacketField(index = 6,len = 2,valExpr = "x/10-40")
    public float maxTemperature;

    //氢系统中最高温度探针代号
    @PacketField(index = 7,len = 1)
    public short maxTemperatureCode;

    //氢气最高浓度
    @PacketField(index = 8,len = 2,valExpr = "x-10000")
    public int maxConcentration;

    //氢气最高浓度传感器代号
    @PacketField(index = 9,len = 1)
    public short maxConcentrationCode;

    //氢气最高压力
    @PacketField(index = 10,len = 2,valExpr = "x/10")
    public float maxPressure;

    //氢气最高压力传感器代号
    @PacketField(index = 11,len = 1)
    public short maxPressureCode;

    //高压DC/DC状态
    @PacketField(index = 12,len = 1)
    public short dcStatus;

}

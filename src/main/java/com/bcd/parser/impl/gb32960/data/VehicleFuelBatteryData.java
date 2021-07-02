package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 燃料电池数据
 */
@Parsable
public class VehicleFuelBatteryData {
    //燃料电池电压
    @PacketField(index = 1,len = 2,valExpr = "x/10")
    float voltage;

    //燃料电池电流
    @PacketField(index = 2,len = 2,valExpr = "x/10")
    float current;

    //燃料消耗率
    @PacketField(index = 3,len = 2,valExpr = "x/100")
    float consumptionRate;

    //燃料电池温度探针总数
    @PacketField(index = 4,len = 2,var = 'a')
    int num;

    //探针温度值
    @PacketField(index =5,lenExpr = "a",valExpr = "x-40",singleLen = 1)
    short[] temperatures;

    //氢系统中最高温度
    @PacketField(index = 6,len = 2,valExpr = "x/10-40")
    float maxTemperature;

    //氢系统中最高温度探针代号
    @PacketField(index = 7,len = 1)
    short maxTemperatureCode;

    //氢气最高浓度
    @PacketField(index = 8,len = 2,valExpr = "x-10000")
    int maxConcentration;

    //氢气最高浓度传感器代号
    @PacketField(index = 9,len = 1)
    short maxConcentrationCode;

    //氢气最高压力
    @PacketField(index = 10,len = 2,valExpr = "x/10")
    float maxPressure;

    //氢气最高压力传感器代号
    @PacketField(index = 11,len = 1)
    short maxPressureCode;

    //高压DC/DC状态
    @PacketField(index = 12,len = 1)
    short dcStatus;


    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(float consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public short[] getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(short[] temperatures) {
        this.temperatures = temperatures;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public short getMaxTemperatureCode() {
        return maxTemperatureCode;
    }

    public void setMaxTemperatureCode(short maxTemperatureCode) {
        this.maxTemperatureCode = maxTemperatureCode;
    }

    public int getMaxConcentration() {
        return maxConcentration;
    }

    public void setMaxConcentration(int maxConcentration) {
        this.maxConcentration = maxConcentration;
    }

    public short getMaxConcentrationCode() {
        return maxConcentrationCode;
    }

    public void setMaxConcentrationCode(short maxConcentrationCode) {
        this.maxConcentrationCode = maxConcentrationCode;
    }

    public float getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(float maxPressure) {
        this.maxPressure = maxPressure;
    }

    public short getMaxPressureCode() {
        return maxPressureCode;
    }

    public void setMaxPressureCode(short maxPressureCode) {
        this.maxPressureCode = maxPressureCode;
    }

    public short getDcStatus() {
        return dcStatus;
    }

    public void setDcStatus(short dcStatus) {
        this.dcStatus = dcStatus;
    }
}

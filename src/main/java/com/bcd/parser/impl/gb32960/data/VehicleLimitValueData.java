package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 极值数据
 */
@Parsable
public class VehicleLimitValueData {
    //最高电压电池子系统号
    @PacketField(index = 1,len = 1)
    short maxVoltageSystemNo;

    //最高电压电池单体代号
    @PacketField(index = 2,len = 1)
    short maxVoltageCode;

    //电池单体电压最高值
    @PacketField(index = 3,len = 2,valExpr = "x/1000")
    float maxVoltage;

    //最低电压电池子系统号
    @PacketField(index = 4,len = 1)
    short minVoltageSystemNo;

    //最低电压电池单体代号
    @PacketField(index = 5,len = 1)
    short minVoltageCode;

    //电池单体电压最低值
    @PacketField(index = 6,len = 2,valExpr = "x/1000")
    float minVoltage;

    //最高温度子系统号
    @PacketField(index = 7,len = 1)
    short maxTemperatureSystemNo;

    //最高温度探针序号
    @PacketField(index = 8,len = 1)
    short maxTemperatureNo;

    //最高温度值
    @PacketField(index = 9,len = 1,valExpr = "x-40")
    short maxTemperature;

    //最低温度子系统号
    @PacketField(index = 10,len = 1)
    short minTemperatureSystemNo;

    //最低温度探针序号
    @PacketField(index = 11,len = 1)
    short minTemperatureNo;

    //最低温度值
    @PacketField(index = 12,len = 1,valExpr = "x-40")
    short minTemperature;

    public short getMaxVoltageSystemNo() {
        return maxVoltageSystemNo;
    }

    public void setMaxVoltageSystemNo(short maxVoltageSystemNo) {
        this.maxVoltageSystemNo = maxVoltageSystemNo;
    }

    public short getMaxVoltageCode() {
        return maxVoltageCode;
    }

    public void setMaxVoltageCode(short maxVoltageCode) {
        this.maxVoltageCode = maxVoltageCode;
    }

    public float getMaxVoltage() {
        return maxVoltage;
    }

    public void setMaxVoltage(float maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    public short getMinVoltageSystemNo() {
        return minVoltageSystemNo;
    }

    public void setMinVoltageSystemNo(short minVoltageSystemNo) {
        this.minVoltageSystemNo = minVoltageSystemNo;
    }

    public short getMinVoltageCode() {
        return minVoltageCode;
    }

    public void setMinVoltageCode(short minVoltageCode) {
        this.minVoltageCode = minVoltageCode;
    }

    public float getMinVoltage() {
        return minVoltage;
    }

    public void setMinVoltage(float minVoltage) {
        this.minVoltage = minVoltage;
    }

    public short getMaxTemperatureSystemNo() {
        return maxTemperatureSystemNo;
    }

    public void setMaxTemperatureSystemNo(short maxTemperatureSystemNo) {
        this.maxTemperatureSystemNo = maxTemperatureSystemNo;
    }

    public short getMaxTemperatureNo() {
        return maxTemperatureNo;
    }

    public void setMaxTemperatureNo(short maxTemperatureNo) {
        this.maxTemperatureNo = maxTemperatureNo;
    }

    public short getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(short maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public short getMinTemperatureSystemNo() {
        return minTemperatureSystemNo;
    }

    public void setMinTemperatureSystemNo(short minTemperatureSystemNo) {
        this.minTemperatureSystemNo = minTemperatureSystemNo;
    }

    public short getMinTemperatureNo() {
        return minTemperatureNo;
    }

    public void setMinTemperatureNo(short minTemperatureNo) {
        this.minTemperatureNo = minTemperatureNo;
    }

    public short getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(short minTemperature) {
        this.minTemperature = minTemperature;
    }
}

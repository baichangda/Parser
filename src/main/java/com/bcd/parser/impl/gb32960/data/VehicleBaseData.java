package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 整车数据
 */
@Parsable
public class VehicleBaseData {
    //车辆状态
    @PacketField(index = 1, len = 1)
    short vehicleStatus;

    //充电状态
    @PacketField(index = 2, len = 1)
    short chargeStatus;

    //运行模式
    @PacketField(index = 3, len = 1)
    short runMode;

    //车速
    @PacketField(index = 4, len = 2, valExpr = "x/10")
    float vehicleSpeed;

    //累计里程
    @PacketField(index = 5, len = 4, valExpr = "x/10")
    double totalMileage;

    //总电压
    @PacketField(index = 6, len = 2, valExpr = "x/10")
    float totalVoltage;

    //总电流
    @PacketField(index = 7, len = 2, valExpr = "x/10-1000")
    float totalCurrent;

    //soc
    @PacketField(index = 8, len = 1)
    short soc;

    //DC-DC状态
    @PacketField(index = 9, len = 1)
    short dcStatus;

    //档位
    @PacketField(index = 9, len = 1)
    short gearPosition;

    //绝缘电阻
    @PacketField(index = 10, len = 2)
    int resistance;

    //加速踏板行程值
    @PacketField(index = 11, len = 1)
    short pedalVal;

    //制动踏板状态
    @PacketField(index = 12, len = 1)
    short pedalStatus;


    public short getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(short vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public short getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(short chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public short getRunMode() {
        return runMode;
    }

    public void setRunMode(short runMode) {
        this.runMode = runMode;
    }

    public float getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(float vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public double getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(double totalMileage) {
        this.totalMileage = totalMileage;
    }

    public float getTotalVoltage() {
        return totalVoltage;
    }

    public void setTotalVoltage(float totalVoltage) {
        this.totalVoltage = totalVoltage;
    }

    public float getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(float totalCurrent) {
        this.totalCurrent = totalCurrent;
    }

    public short getSoc() {
        return soc;
    }

    public void setSoc(short soc) {
        this.soc = soc;
    }

    public short getDcStatus() {
        return dcStatus;
    }

    public void setDcStatus(short dcStatus) {
        this.dcStatus = dcStatus;
    }

    public short getGearPosition() {
        return gearPosition;
    }

    public void setGearPosition(short gearPosition) {
        this.gearPosition = gearPosition;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public short getPedalVal() {
        return pedalVal;
    }

    public void setPedalVal(short pedalVal) {
        this.pedalVal = pedalVal;
    }

    public short getPedalStatus() {
        return pedalStatus;
    }

    public void setPedalStatus(short pedalStatus) {
        this.pedalStatus = pedalStatus;
    }
}

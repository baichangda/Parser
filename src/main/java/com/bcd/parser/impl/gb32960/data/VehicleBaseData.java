package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 整车数据
 */
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
    @PacketField(index = 4,len=2
//            ,valExpr = "x*0.1"
    )
    int vehicleSpeed;

    //累计里程
    @PacketField(index = 5,len=4
//            ,valExpr = "x*0.1"
    )
    long totalMileage;

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

    public int getTotalVoltage() {
        return totalVoltage;
    }

    public void setTotalVoltage(int totalVoltage) {
        this.totalVoltage = totalVoltage;
    }

    public int getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(int totalCurrent) {
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

    public int getGearPosition() {
        return gearPosition;
    }

    public void setGearPosition(int gearPosition) {
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

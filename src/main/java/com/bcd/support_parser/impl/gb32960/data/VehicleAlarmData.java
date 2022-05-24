package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 报警数据
 */
@Parsable
public class VehicleAlarmData {
    //最高报警等级
    @PacketField(index = 1,len = 1)
    short maxAlarmLevel;

    //最高电压电池单体代号
    @PacketField(index = 2,len = 4)
    int alarmFlag;

    //可充电储能装置故障总数
    @PacketField(index = 3,len = 1,var = 'a')
    short chargeBadNum;

    //可充电储能装置故障代码列表
    @PacketField(index = 4,lenExpr = "a*4",singleLen = 4)
    long[] chargeBadCodes;

    //驱动电机故障总数
    @PacketField(index = 5,len = 1,var = 'b')
    short driverBadNum;

    //驱动电机故障代码列表
    @PacketField(index = 6,lenExpr = "b*4",singleLen = 4)
    long[] driverBadCodes;

    //驱动电机故障总数
    @PacketField(index = 7,len = 1,var = 'c')
    short engineBadNum;

    //驱动电机故障代码列表
    @PacketField(index = 8,lenExpr = "c*4",singleLen = 4)
    long[] engineBadCodes;

    //其他故障总数
    @PacketField(index = 9,len = 1,var = 'd')
    short otherBadNum;

    //其他故障代码列表
    @PacketField(index = 10,lenExpr = "d*4",singleLen = 4)
    long[] otherBadCodes;

    public short getMaxAlarmLevel() {
        return maxAlarmLevel;
    }

    public void setMaxAlarmLevel(short maxAlarmLevel) {
        this.maxAlarmLevel = maxAlarmLevel;
    }

    public int getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(int alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public short getChargeBadNum() {
        return chargeBadNum;
    }

    public void setChargeBadNum(short chargeBadNum) {
        this.chargeBadNum = chargeBadNum;
    }

    public long[] getChargeBadCodes() {
        return chargeBadCodes;
    }

    public void setChargeBadCodes(long[] chargeBadCodes) {
        this.chargeBadCodes = chargeBadCodes;
    }

    public short getDriverBadNum() {
        return driverBadNum;
    }

    public void setDriverBadNum(short driverBadNum) {
        this.driverBadNum = driverBadNum;
    }

    public long[] getDriverBadCodes() {
        return driverBadCodes;
    }

    public void setDriverBadCodes(long[] driverBadCodes) {
        this.driverBadCodes = driverBadCodes;
    }

    public short getEngineBadNum() {
        return engineBadNum;
    }

    public void setEngineBadNum(short engineBadNum) {
        this.engineBadNum = engineBadNum;
    }

    public long[] getEngineBadCodes() {
        return engineBadCodes;
    }

    public void setEngineBadCodes(long[] engineBadCodes) {
        this.engineBadCodes = engineBadCodes;
    }

    public short getOtherBadNum() {
        return otherBadNum;
    }

    public void setOtherBadNum(short otherBadNum) {
        this.otherBadNum = otherBadNum;
    }

    public long[] getOtherBadCodes() {
        return otherBadCodes;
    }

    public void setOtherBadCodes(long[] otherBadCodes) {
        this.otherBadCodes = otherBadCodes;
    }
}

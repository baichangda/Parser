package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_integer_array;
import com.bcd.support_parser.anno.F_integer;

/**
 * 报警数据
 */
public class VehicleAlarmData {
    //最高报警等级
    @F_integer(len = 1)
    public short maxAlarmLevel;

    //最高电压电池单体代号
    @F_integer(len = 4)
    public int alarmFlag;

    //可充电储能装置故障总数
    @F_integer(len = 1, var = 'a')
    public short chargeBadNum;

    //可充电储能装置故障代码列表
    @F_integer_array(lenExpr = "a*4", singleLen = 4)
    public long[] chargeBadCodes;

    //驱动电机故障总数
    @F_integer(len = 1, var = 'b')
    public short driverBadNum;

    //驱动电机故障代码列表
    @F_integer_array(lenExpr = "b*4", singleLen = 4)
    public int[] driverBadCodes;

    //发动机故障总数
    @F_integer(len = 1, var = 'c')
    public short engineBadNum;

    //发动机故障代码列表
    @F_integer_array(lenExpr = "c*4", singleLen = 1)
    public short[] engineBadCodes;

    //其他故障总数
    @F_integer(len = 1, var = 'd')
    public short otherBadNum;

    //其他故障代码列表
    @F_integer_array(lenExpr = "d*4", singleLen = 4)
    public long[] otherBadCodes;


}

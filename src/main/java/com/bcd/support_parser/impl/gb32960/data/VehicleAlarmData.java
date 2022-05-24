package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 报警数据
 */
@Parsable
public class VehicleAlarmData {
    //最高报警等级
    @PacketField(index = 1,len = 1)
    public short maxAlarmLevel;

    //最高电压电池单体代号
    @PacketField(index = 2,len = 4)
    public int alarmFlag;

    //可充电储能装置故障总数
    @PacketField(index = 3,len = 1,var = 'a')
    public short chargeBadNum;

    //可充电储能装置故障代码列表
    @PacketField(index = 4,lenExpr = "a*4",singleLen = 4)
    public long[] chargeBadCodes;

    //驱动电机故障总数
    @PacketField(index = 5,len = 1,var = 'b')
    public short driverBadNum;

    //驱动电机故障代码列表
    @PacketField(index = 6,lenExpr = "b*4",singleLen = 4)
    public long[] driverBadCodes;

    //驱动电机故障总数
    @PacketField(index = 7,len = 1,var = 'c')
    public short engineBadNum;

    //驱动电机故障代码列表
    @PacketField(index = 8,lenExpr = "c*4",singleLen = 4)
    public long[] engineBadCodes;

    //其他故障总数
    @PacketField(index = 9,len = 1,var = 'd')
    public short otherBadNum;

    //其他故障代码列表
    @PacketField(index = 10,lenExpr = "d*4",singleLen = 4)
    public long[] otherBadCodes;
}

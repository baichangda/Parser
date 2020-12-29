package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 报警数据
 */
@Getter
@Setter
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
    @PacketField(index = 4,lenExpr = "a*4")
    int[] chargeBadCodes;

    //驱动电机故障总数
    @PacketField(index = 5,len = 1,var = 'b')
    short driverBadNum;

    //驱动电机故障代码列表
    @PacketField(index = 6,lenExpr = "b*4")
    int[] driverBadCodes;

    //驱动电机故障总数
    @PacketField(index = 7,len = 1,var = 'c')
    short engineBadNum;

    //驱动电机故障代码列表
    @PacketField(index = 8,lenExpr = "c*4")
    int[] engineBadCodes;

    //其他故障总数
    @PacketField(index = 9,len = 1,var = 'd')
    short otherBadNum;

    //其他故障代码列表
    @PacketField(index = 10,lenExpr = "d*4")
    int[] otherBadCodes;

}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 每个可充电储能子系统电压数据格式
 */
@Getter
@Setter
@Parsable
public class StorageVoltageData {
    //可充电储能子系统号
    @PacketField(index = 1,len = 1)
    short no;

    //可充电储能装置电压
    @PacketField(index = 2,len = 2)
    int voltage;

    //可充电储能状态电流
    @PacketField(index = 3,len = 2)
    int current;

    //单体电池总数
    @PacketField(index = 4,len = 2)
    int total;

    //本帧起始电池序号
    @PacketField(index = 5,len = 2)
    int frameNo;

    //本帧单体电池总数
    @PacketField(index = 6,len = 1,var = 'm')
    short frameTotal;

    //单体电池电压
    @PacketField(index = 7,singleLen = 2,lenExpr = "2*m")
    int[] singleVoltage;

}

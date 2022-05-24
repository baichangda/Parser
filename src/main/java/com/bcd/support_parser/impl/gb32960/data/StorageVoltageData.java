package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 每个可充电储能子系统电压数据格式
 */
@Parsable
public class StorageVoltageData {
    //可充电储能子系统号
    @PacketField(index = 1, len = 1)
    public short no;

    //可充电储能装置电压
    @PacketField(index = 2, len = 2, valExpr = "x/10",valPrecision = 1)
    public float voltage;

    //可充电储能状态电流
    @PacketField(index = 3, len = 2, valExpr = "(x-10000)/10",valPrecision = 1)
    public float current;

    //单体电池总数
    @PacketField(index = 4, len = 2)
    public int total;

    //本帧起始电池序号
    @PacketField(index = 5, len = 2)
    public int frameNo;

    //本帧单体电池总数
    @PacketField(index = 6, len = 1, var = 'm')
    public short frameTotal;

    //单体电池电压
    @PacketField(index = 7, singleLen = 2, lenExpr = "2*m", valExpr = "x/1000",valPrecision = 3)
    public float[] singleVoltage;
}

package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.*;

/**
 * 每个可充电储能子系统电压数据格式
 */
public class StorageVoltageData {
    //可充电储能子系统号
    @F_integer(len = 1)
    public short no;

    //可充电储能装置电压
    @F_float_integer(len = 2, valExpr = "x/10")
    public float voltage;

    //可充电储能状态电流
    @F_float_integer(len = 2, valExpr = "x/10-1000")
    public float current;

    //单体电池总数
    @F_integer(len = 2)
    public int total;

    //本帧起始电池序号
    @F_integer(len = 2)
    public int frameNo;

    //本帧单体电池总数
    @F_integer(len = 1, var = 'm')
    public short frameTotal;

    //单体电池电压
    @F_float_integer_array(singleLen = 2, lenExpr = "2*m", valExpr = "x/1000")
    public float[] singleVoltage;
}

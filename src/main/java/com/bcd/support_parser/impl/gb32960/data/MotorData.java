package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_float_integer;
import com.bcd.support_parser.anno.F_integer;

/**
 * 每个驱动电机数据格式
 */
public class MotorData {
    //驱动电机序号
    @F_integer(len = 1)
    public short no;

    //驱动电机状态
    @F_integer(len = 1)
    public short status;

    //驱动电机控制器温度
    @F_integer(len = 1, valExpr = "x-40")
    public short controllerTemperature;

    //驱动电机转速
    @F_integer(len = 2, valExpr = "x-20000")
    public int rotateSpeed;

    //驱动电机转矩
    @F_float_integer(len = 2, valExpr = "x/10-2000")
    public float rotateRectangle;

    //驱动电机温度
    @F_integer(len = 1, valExpr = "x-40")
    public short temperature;

    //电机控制器输入电压
    @F_float_integer(len = 2, valExpr = "x/10")
    public float inputVoltage;

    //电机控制器直流母线电流
    @F_float_integer(len = 2, valExpr = "x/10-1000")
    public float current;
}

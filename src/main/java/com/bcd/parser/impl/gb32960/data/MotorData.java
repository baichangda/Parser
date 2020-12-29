package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 每个驱动电机数据格式
 */
@Getter
@Setter
@Parsable
public class MotorData {
    //驱动电机序号
    @PacketField(index = 1,len = 1)
    short no;

    //驱动电机状态
    @PacketField(index = 2,len = 1)
    short status;

    //驱动电机控制器温度
    @PacketField(index = 3,len = 1)
    int controllerTemperature;

    //驱动电机转速
    @PacketField(index = 4,len = 2)
    int rotateSpeed;

    //驱动电机转矩
    @PacketField(index = 5,len = 2)
    int rotateRectangle;

    //驱动电机温度
    @PacketField(index = 6,len = 1)
    short temperature;

    //电机控制器输入电压
    @PacketField(index = 7,len = 2)
    int inputVoltage;

    //电机控制器直流母线电流
    @PacketField(index = 8,len = 2)
    int current;

}

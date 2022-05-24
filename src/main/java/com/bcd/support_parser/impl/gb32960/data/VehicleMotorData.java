package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 驱动电机数据
 */
@Parsable
public class VehicleMotorData {
    //驱动电机个数
    @PacketField(index = 1,len = 1,var = 'a')
    public short num;

    //驱动电机总成信息列表
    @PacketField(index = 2,listLenExpr = "a")
    public  MotorData[] content;
}

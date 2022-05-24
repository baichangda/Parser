package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 发动机数据
 */
@Parsable
public class VehicleEngineData {
    //发动机状态
    @PacketField(index = 1, len = 1)
    public short status;

    //曲轴转速
    @PacketField(index = 2, len = 2)
    public int speed;

    //燃料消耗率
    @PacketField(index = 3, len = 2, valExpr = "x/100", valPrecision = 2)
    public float rate;

}

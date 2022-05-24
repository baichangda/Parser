package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.anno.Parsable;

/**
 * 车辆位置数据
 */
@Parsable
public class VehiclePositionData {
    //定位状态
    @PacketField(index = 1,len = 1)
    public byte status;

    //经度
    @PacketField(index = 2,len = 4,valExpr = "x/1000000",valPrecision = 6)
    public double lng;

    //纬度
    @PacketField(index = 3,len = 4,valExpr = "x/1000000",valPrecision = 6)
    public double lat;
}

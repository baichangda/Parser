package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.PacketField;

/**
 * 车辆位置数据
 */
public class VehiclePositionData {
    //定位状态
    @PacketField(index = 1,len = 1)
    public byte status;

    //经度
    @PacketField(index = 2,len = 4,valExpr = "x/1000000")
    public double lng;

    //纬度
    @PacketField(index = 3,len = 4,valExpr = "x/1000000")
    public double lat;
}

package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_float_integer;
import com.bcd.support_parser.anno.F_integer;

/**
 * 车辆位置数据
 */
public class VehiclePositionData {
    //定位状态
    @F_integer(len = 1)
    public byte status;

    //经度
    @F_float_integer(len = 4,valExpr = "x/1000000")
    public double lng;

    //纬度
    @F_float_integer(len = 4,valExpr = "x/1000000")
    public double lat;
}

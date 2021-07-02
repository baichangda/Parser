package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 车辆位置数据
 */
@Parsable
public class VehiclePositionData {
    //定位状态
    @PacketField(index = 1,len = 1)
    byte status;

    //经度
    @PacketField(index = 2,len = 4,valExpr = "x/1000000")
    double lng;

    //纬度
    @PacketField(index = 3,len = 4,valExpr = "x/1000000")
    double lat;


    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}

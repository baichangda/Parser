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
    @PacketField(index = 2,len = 4)
    int lng;

    //纬度
    @PacketField(index = 3,len = 4)
    int lat;

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }
}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 车辆位置数据
 */
@Getter
@Setter
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

}

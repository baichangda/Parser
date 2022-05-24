package com.bcd.support_parser.impl.gb32960.data;


import com.bcd.support_parser.anno.PacketField;
import com.bcd.support_parser.impl.gb32960.javassist.processor.VehicleCommonDataFieldProcessor;

import java.util.Date;

public class VehicleSupplementData implements PacketData {
    //数据采集时间
    @PacketField(index = 1,len = 6)
    public Date collectTime;

    //车辆运行通用数据
    @PacketField(index = 2, processorClass = VehicleCommonDataFieldProcessor.class)
    public VehicleCommonData vehicleCommonData;
}

package com.bcd.support_parser.impl.gb32960.data;


import com.bcd.support_parser.anno.F_date;
import com.bcd.support_parser.anno.F_userDefine;
import com.bcd.support_parser.impl.gb32960.processor.VehicleCommonDataFieldProcessor;

public class VehicleSupplementData implements PacketData {
    //数据采集时间
    @F_date
    public long collectTime;

    //车辆运行通用数据
    @F_userDefine(processorClass = VehicleCommonDataFieldProcessor.class)
    public VehicleCommonData vehicleCommonData;
}

package com.bcd.parser.impl.gb32960.data;


import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import com.bcd.parser.impl.gb32960.processer.VehicleCommonDataFieldProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Parsable
public class VehicleSupplementData extends PacketData {
    //数据采集时间
    @PacketField(index = 1,len = 6)
    Date collectTime;

    //车辆运行通用数据
    @PacketField(index = 2, processorClass = VehicleCommonDataFieldProcessor.class)
    VehicleCommonData vehicleCommonData;

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 发动机数据
 */
@Getter
@Setter
@Parsable
public class VehicleEngineData {
    //发动机状态
    @PacketField(index = 1,len = 1)
    short status;

    //曲轴转速
    @PacketField(index = 2,len = 2)
    int speed;

    //燃料消耗率
    @PacketField(index = 3,len = 2)
    int rate;


}

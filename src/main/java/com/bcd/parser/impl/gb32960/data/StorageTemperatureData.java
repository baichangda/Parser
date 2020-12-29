package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 每个可充电储能子系统上温度数据格式
 */
@Getter
@Setter
@Parsable
public class StorageTemperatureData {
    //可充电储能子系统号
    @PacketField(index = 1,len = 1)
    short no;

    //可充电储能温度探针个数
    @PacketField(index = 2,len = 2,var = 'n')
    int num;

    //可充电储能子系统各温度探针检测到的温度值
    @PacketField(index = 3,lenExpr = "n")
    short[] temperatures;
}

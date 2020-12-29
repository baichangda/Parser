package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 驱动电机数据
 */
@Getter
@Setter
@Parsable
public class VehicleMotorData {
    //驱动电机个数
    @PacketField(index = 1,len = 1,var = 'a')
    short num;

    //驱动电机总成信息列表
    @PacketField(index = 2,listLenExpr = "a")
    List<MotorData> content=new ArrayList<>();

}

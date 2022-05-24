package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

import java.util.ArrayList;
import java.util.List;

/**
 * 驱动电机数据
 */
@Parsable
public class VehicleMotorData {
    //驱动电机个数
    @PacketField(index = 1,len = 1,var = 'a')
    short num;

    //驱动电机总成信息列表
    @PacketField(index = 2,listLenExpr = "a")
    MotorData[] content;

    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public MotorData[] getContent() {
        return content;
    }

    public void setContent(MotorData[] content) {
        this.content = content;
    }
}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.PacketField;
import com.bcd.parser.anno.Parsable;

/**
 * 发动机数据
 */
@Parsable
public class VehicleEngineData {
    //发动机状态
    @PacketField(index = 1,len = 1)
    short status;

    //曲轴转速
    @PacketField(index = 2,len = 2)
    int speed;

    //燃料消耗率
    @PacketField(index = 3,len = 2,valExpr = "x/100")
    float rate;

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}

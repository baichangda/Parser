package com.bcd.gb32960.data;

import com.bcd.parser.anno.Parsable;

import java.util.Date;

@Parsable
public class PacketData {
    protected String vin;
    protected Date createTime;
    protected String hex;
    protected short flag;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Date getCollectTime(){
        return null;
    }

    public void setCollectTime(Date collectTime){

    }

    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }


}

package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Parsable
public class PacketData {
    protected String vin;
    protected Date createTime;
    protected String hex;
    protected short flag;

    public Date getCollectTime(){
        return null;
    }

    public void setCollectTime(Date collectTime){

    }
}

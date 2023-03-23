package com.bcd.support_parser.impl.raco.data;

import com.bcd.support_parser.anno.ByteOrder;
import com.bcd.support_parser.anno.F_float_ieee754;
import com.bcd.support_parser.anno.F_integer;
import com.bcd.support_parser.anno.FloatType_ieee754;

public class Packet_header {
    @F_integer(len = 4, order = ByteOrder.SmallEndian)
    public long header;
    @F_integer(len = 2, order = ByteOrder.SmallEndian)
    public int header_len;
    @F_integer(len = 4, order = ByteOrder.SmallEndian)
    public long frame_len;
    @F_integer(len = 2, order = ByteOrder.SmallEndian)
    public int frame_type;
    @F_integer(len = 4, order = ByteOrder.SmallEndian)
    public long version;
    @F_integer(len = 4, order = ByteOrder.SmallEndian)
    public long device_sn;
    @F_integer(len = 4, order = ByteOrder.SmallEndian)
    public long count;
    @F_float_ieee754(type = FloatType_ieee754.DOUBLE, order = ByteOrder.SmallEndian)
    public double timestamp;
    @F_integer(len = 2, order = ByteOrder.SmallEndian)
    public int fps;
    @F_float_ieee754(type = FloatType_ieee754.DOUBLE, order = ByteOrder.SmallEndian)
    public double dev_lon;
    @F_float_ieee754(type = FloatType_ieee754.DOUBLE, order = ByteOrder.SmallEndian)
    public double dev_lat;
    @F_float_ieee754(type = FloatType_ieee754.FLOAT, order = ByteOrder.SmallEndian)
    public float dev_alt;
    @F_float_ieee754(type = FloatType_ieee754.FLOAT, order = ByteOrder.SmallEndian)
    public float dev_azimuth;
    @F_integer(len = 1, order = ByteOrder.SmallEndian)
    public byte reserved;
}

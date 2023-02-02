package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.F_bean_list;
import com.bcd.support_parser.anno.F_integer;

import java.util.List;

/**
 * 驱动电机数据
 */
public class VehicleMotorData {
    //驱动电机个数
    @F_integer(len = 1,var = 'a')
    public short num;

    //驱动电机总成信息列表
    @F_bean_list(listLenExpr = "a")
    public List<MotorData> content;
}

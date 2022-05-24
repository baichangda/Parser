package com.bcd.support_parser.impl.gb32960.data;

import com.bcd.support_parser.anno.Parsable;

/**
 * 车辆运行通用数据
 */
@Parsable
public class VehicleCommonData {
    //整车数据
    public VehicleBaseData vehicleBaseData;
    //驱动电机数据
    public VehicleMotorData vehicleMotorData;
    //燃料电池数据
    public VehicleFuelBatteryData vehicleFuelBatteryData;
    //发动机数据
    public VehicleEngineData vehicleEngineData;
    //车辆位置数据
    public VehiclePositionData vehiclePositionData;
    //极值数据
    public VehicleLimitValueData vehicleLimitValueData;
    //报警数据
    public VehicleAlarmData vehicleAlarmData;
    //可充电储能装置电压数据
    public VehicleStorageVoltageData vehicleStorageVoltageData;
    //可充电储能装置温度数据
    public VehicleStorageTemperatureData vehicleStorageTemperatureData;
}

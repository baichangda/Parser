package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.Parsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 车辆运行通用数据
 */
@Getter
@Setter
@Parsable
public class VehicleCommonData {
    //整车数据
    VehicleBaseData vehicleBaseData;
    //驱动电机数据
    VehicleMotorData vehicleMotorData;
    //燃料电池数据
    VehicleFuelBatteryData vehicleFuelBatteryData;
    //发动机数据
    VehicleEngineData vehicleEngineData;
    //车辆位置数据
    VehiclePositionData vehiclePositionData;
    //极值数据
    VehicleLimitValueData vehicleLimitValueData;
    //报警数据
    VehicleAlarmData vehicleAlarmData;
    //可充电储能装置电压数据
    VehicleStorageVoltageData vehicleStorageVoltageData;
    //可充电储能装置温度数据
    VehicleStorageTemperatureData vehicleStorageTemperatureData;

}

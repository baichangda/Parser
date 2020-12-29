package com.bcd.parser.impl.gb32960.data;

import com.bcd.parser.anno.Parsable;

/**
 * 车辆运行通用数据
 */
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

    public VehicleBaseData getVehicleBaseData() {
        return vehicleBaseData;
    }

    public void setVehicleBaseData(VehicleBaseData vehicleBaseData) {
        this.vehicleBaseData = vehicleBaseData;
    }

    public VehicleMotorData getVehicleMotorData() {
        return vehicleMotorData;
    }

    public void setVehicleMotorData(VehicleMotorData vehicleMotorData) {
        this.vehicleMotorData = vehicleMotorData;
    }

    public VehicleFuelBatteryData getVehicleFuelBatteryData() {
        return vehicleFuelBatteryData;
    }

    public void setVehicleFuelBatteryData(VehicleFuelBatteryData vehicleFuelBatteryData) {
        this.vehicleFuelBatteryData = vehicleFuelBatteryData;
    }

    public VehicleEngineData getVehicleEngineData() {
        return vehicleEngineData;
    }

    public void setVehicleEngineData(VehicleEngineData vehicleEngineData) {
        this.vehicleEngineData = vehicleEngineData;
    }

    public VehiclePositionData getVehiclePositionData() {
        return vehiclePositionData;
    }

    public void setVehiclePositionData(VehiclePositionData vehiclePositionData) {
        this.vehiclePositionData = vehiclePositionData;
    }

    public VehicleLimitValueData getVehicleLimitValueData() {
        return vehicleLimitValueData;
    }

    public void setVehicleLimitValueData(VehicleLimitValueData vehicleLimitValueData) {
        this.vehicleLimitValueData = vehicleLimitValueData;
    }

    public VehicleAlarmData getVehicleAlarmData() {
        return vehicleAlarmData;
    }

    public void setVehicleAlarmData(VehicleAlarmData vehicleAlarmData) {
        this.vehicleAlarmData = vehicleAlarmData;
    }

    public VehicleStorageVoltageData getVehicleStorageVoltageData() {
        return vehicleStorageVoltageData;
    }

    public void setVehicleStorageVoltageData(VehicleStorageVoltageData vehicleStorageVoltageData) {
        this.vehicleStorageVoltageData = vehicleStorageVoltageData;
    }

    public VehicleStorageTemperatureData getVehicleStorageTemperatureData() {
        return vehicleStorageTemperatureData;
    }

    public void setVehicleStorageTemperatureData(VehicleStorageTemperatureData vehicleStorageTemperatureData) {
        this.vehicleStorageTemperatureData = vehicleStorageTemperatureData;
    }
}

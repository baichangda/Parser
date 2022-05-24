package com.bcd.support_parser.impl.gb32960.processor;

import com.bcd.support_parser.impl.gb32960.data.*;
import com.bcd.support_parser.processer.FieldDeProcessContext;
import com.bcd.support_parser.processer.FieldProcessContext;
import com.bcd.support_parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleCommonDataFieldProcessor extends FieldProcessor<VehicleCommonData> {
    Logger logger = LoggerFactory.getLogger(VehicleCommonDataFieldProcessor.class);

    @Override
    public VehicleCommonData process(ByteBuf byteBuf, FieldProcessContext processContext) {
        VehicleCommonData vehicleCommonData = new VehicleCommonData();
        int allLen = ((Packet) processContext.parentContext.instance).contentLength - 6;
        int beginLeave = byteBuf.readableBytes();
        A:
        while (byteBuf.isReadable()) {
            int curLeave = byteBuf.readableBytes();
            if (beginLeave - curLeave >= allLen) {
                break;
            }
            short flag = byteBuf.readUnsignedByte();
            switch (flag) {
                case 1: {
                    //2.1、整车数据
                    VehicleBaseData data = parser.parse(VehicleBaseData.class, byteBuf);
                    vehicleCommonData.vehicleBaseData = data;
                    break;
                }
                case 2: {
                    //2.2、驱动电机数据
                    VehicleMotorData vehicleMotorData = parser.parse(VehicleMotorData.class, byteBuf);
                    vehicleCommonData.vehicleMotorData = vehicleMotorData;
                    break;
                }
                case 3: {
                    //2.3、燃料电池数据
                    VehicleFuelBatteryData vehicleFuelBatteryData = parser.parse(VehicleFuelBatteryData.class, byteBuf);
                    vehicleCommonData.vehicleFuelBatteryData = vehicleFuelBatteryData;
                    break;
                }
                case 4: {
                    //2.4、发动机数据
                    VehicleEngineData data = parser.parse(VehicleEngineData.class, byteBuf);
                    vehicleCommonData.vehicleEngineData = data;
                    break;
                }
                case 5: {
                    //2.5、车辆位置数据
                    VehiclePositionData data = parser.parse(VehiclePositionData.class, byteBuf);
                    vehicleCommonData.vehiclePositionData = data;
                    break;
                }
                case 6: {
                    //2.6、极值数据
                    VehicleLimitValueData data = parser.parse(VehicleLimitValueData.class, byteBuf);
                    vehicleCommonData.vehicleLimitValueData = data;
                    break;
                }
                case 7: {
                    //2.7、报警数据
                    VehicleAlarmData vehicleAlarmData = parser.parse(VehicleAlarmData.class, byteBuf);
                    vehicleCommonData.vehicleAlarmData = vehicleAlarmData;
                    break;
                }
                case 8: {
                    //2.8、可充电储能装置电压数据
                    VehicleStorageVoltageData vehicleStorageVoltageData = parser.parse(VehicleStorageVoltageData.class, byteBuf);
                    vehicleCommonData.vehicleStorageVoltageData = vehicleStorageVoltageData;
                    break;
                }
                case 9: {
                    //2.9、可充电储能装置温度数据
                    VehicleStorageTemperatureData vehicleStorageTemperatureData = parser.parse(VehicleStorageTemperatureData.class, byteBuf);
                    vehicleCommonData.vehicleStorageTemperatureData = vehicleStorageTemperatureData;
                    break;
                }
                default: {
                    logger.warn("Parse Vehicle Common Data Interrupted,Unknown Flag[" + flag + "]");
                    //2.8、如果是自定义数据,只做展现,不解析
                    //2.8.1、解析长度
                    int dataLen = byteBuf.readUnsignedShort();
                    //2.8.2、获取接下来的报文
                    byte[] content = new byte[dataLen];
                    byteBuf.getBytes(0, content);
                    break A;
//                      throw BaseRuntimeException.getException("Parse Vehicle Data Failed,Unknown Flag["+flag+"]");
                }
            }
        }
        return vehicleCommonData;
    }

    @Override
    public void deProcess(VehicleCommonData data, ByteBuf result, FieldDeProcessContext processContext) {
        if (data.vehicleBaseData != null) {
            result.writeByte(1);
            result.writeBytes(parser.toByteBuf(data.vehicleBaseData));
        }
        if (data.vehicleMotorData != null) {
            result.writeByte(2);
            result.writeBytes(parser.toByteBuf(data.vehicleMotorData));
        }
        if (data.vehicleFuelBatteryData != null) {
            result.writeByte(3);
            result.writeBytes(parser.toByteBuf(data.vehicleFuelBatteryData));
        }
        if (data.vehicleEngineData != null) {
            result.writeByte(4);
            result.writeBytes(parser.toByteBuf(data.vehicleEngineData));
        }
        if (data.vehiclePositionData != null) {
            result.writeByte(5);
            result.writeBytes(parser.toByteBuf(data.vehiclePositionData));
        }
        if (data.vehicleLimitValueData != null) {
            result.writeByte(6);
            result.writeBytes(parser.toByteBuf(data.vehicleLimitValueData));
        }
        if (data.vehicleAlarmData != null) {
            result.writeByte(7);
            result.writeBytes(parser.toByteBuf(data.vehicleAlarmData));
        }
        if (data.vehicleStorageVoltageData != null) {
            result.writeByte(8);
            result.writeBytes(parser.toByteBuf(data.vehicleStorageVoltageData));
        }
        if (data.vehicleStorageTemperatureData != null) {
            result.writeByte(9);
            result.writeBytes(parser.toByteBuf(data.vehicleStorageTemperatureData));
        }
    }
}

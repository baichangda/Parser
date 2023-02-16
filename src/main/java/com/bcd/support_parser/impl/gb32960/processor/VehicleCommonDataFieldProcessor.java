package com.bcd.support_parser.impl.gb32960.javassist.processor;

import com.bcd.support_parser.impl.gb32960.data.*;
import com.bcd.support_parser.javassist.processor.Processor;
import com.bcd.support_parser.javassist.processor.ProcessContext;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleCommonDataFieldProcessor extends Processor<VehicleCommonData> {
    Logger logger = LoggerFactory.getLogger(VehicleCommonDataFieldProcessor.class);

    @Override
    public VehicleCommonData process(final ByteBuf byteBuf, final ProcessContext parentContext) {
        final VehicleCommonData vehicleCommonData = new VehicleCommonData();
        int allLen = ((Packet) parentContext.parentContext.instance).contentLength - 6;
        ProcessContext<VehicleCommonData> processContext = new ProcessContext<>(vehicleCommonData, parentContext);
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
                    vehicleCommonData.vehicleBaseData = parser.parse(VehicleBaseData.class, byteBuf, processContext);
                    break;
                }
                case 2: {
                    //2.2、驱动电机数据
                    vehicleCommonData.vehicleMotorData = parser.parse(VehicleMotorData.class, byteBuf, processContext);
                    break;
                }
                case 3: {
                    //2.3、燃料电池数据
                    vehicleCommonData.vehicleFuelBatteryData = parser.parse(VehicleFuelBatteryData.class, byteBuf, processContext);
                    break;
                }
                case 4: {
                    //2.4、发动机数据
                    vehicleCommonData.vehicleEngineData = parser.parse(VehicleEngineData.class, byteBuf, processContext);
                    break;
                }
                case 5: {
                    //2.5、车辆位置数据
                    vehicleCommonData.vehiclePositionData = parser.parse(VehiclePositionData.class, byteBuf, processContext);
                    break;
                }
                case 6: {
                    //2.6、极值数据
                    vehicleCommonData.vehicleLimitValueData = parser.parse(VehicleLimitValueData.class, byteBuf, processContext);
                    break;
                }
                case 7: {
                    //2.7、报警数据
                    vehicleCommonData.vehicleAlarmData = parser.parse(VehicleAlarmData.class, byteBuf, processContext);
                    break;
                }
                case 8: {
                    //2.8、可充电储能装置电压数据
                    vehicleCommonData.vehicleStorageVoltageData = parser.parse(VehicleStorageVoltageData.class, byteBuf, processContext);
                    break;
                }
                case 9: {
                    //2.9、可充电储能装置温度数据
                    vehicleCommonData.vehicleStorageTemperatureData = parser.parse(VehicleStorageTemperatureData.class, byteBuf, processContext);
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
    public void deProcess(ByteBuf data, ProcessContext parentContext, VehicleCommonData instance) {
        ProcessContext<VehicleCommonData> processContext = new ProcessContext<>(instance, parentContext);
        if (instance.vehicleBaseData!=null) {
            data.writeByte(1);
            parser.deParse(instance.vehicleBaseData,data,processContext);
        }
        if (instance.vehicleMotorData!=null) {
            data.writeByte(2);
            parser.deParse(instance.vehicleMotorData,data,processContext);
        }
        if (instance.vehicleFuelBatteryData!=null) {
            data.writeByte(3);
            parser.deParse(instance.vehicleFuelBatteryData,data,processContext);
        }
        if (instance.vehicleEngineData!=null) {
            data.writeByte(4);
            parser.deParse(instance.vehicleEngineData,data,processContext);
        }
        if (instance.vehiclePositionData!=null) {
            data.writeByte(5);
            parser.deParse(instance.vehiclePositionData,data,processContext);
        }
        if (instance.vehicleLimitValueData!=null) {
            data.writeByte(6);
            parser.deParse(instance.vehicleLimitValueData,data,processContext);
        }
        if (instance.vehicleAlarmData!=null) {
            data.writeByte(7);
            parser.deParse(instance.vehicleAlarmData,data,processContext);
        }
        if (instance.vehicleStorageVoltageData!=null) {
            data.writeByte(8);
            parser.deParse(instance.vehicleStorageVoltageData,data,processContext);
        }
        if (instance.vehicleStorageTemperatureData!=null) {
            data.writeByte(9);
            parser.deParse(instance.vehicleStorageTemperatureData,data,processContext);
        }
    }
}

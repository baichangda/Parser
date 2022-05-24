package com.bcd.parser.impl.gb32960.processor;

import com.bcd.parser.impl.gb32960.data.*;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleCommonDataFieldProcessor extends FieldProcessor<VehicleCommonData> {
    Logger logger= LoggerFactory.getLogger(VehicleCommonDataFieldProcessor.class);

    @Override
    public VehicleCommonData process(ByteBuf byteBuf, FieldProcessContext processContext) {
        VehicleCommonData vehicleCommonData=new VehicleCommonData();
        int allLen= ((Packet)processContext.parentContext.instance).getContentLength()-6;
        int beginLeave=byteBuf.readableBytes();
        A:while(byteBuf.isReadable()) {
            int curLeave=byteBuf.readableBytes();
            if(beginLeave-curLeave>=allLen){
                break;
            }
            short flag = byteBuf.readUnsignedByte();
            switch (flag) {
                case 1: {
                    //2.1、整车数据
                    VehicleBaseData data = parser.parse(VehicleBaseData.class,byteBuf);
                    vehicleCommonData.setVehicleBaseData(data);
                    break;
                }
                case 2: {
                    //2.2、驱动电机数据
                    VehicleMotorData vehicleMotorData= parser.parse(VehicleMotorData.class,byteBuf);
                    vehicleCommonData.setVehicleMotorData(vehicleMotorData);
                    break;
                }
                case 3: {
                    //2.3、燃料电池数据
                    VehicleFuelBatteryData vehicleFuelBatteryData= parser.parse(VehicleFuelBatteryData.class,byteBuf);
                    vehicleCommonData.setVehicleFuelBatteryData(vehicleFuelBatteryData);
                    break;
                }
                case 4: {
                    //2.4、发动机数据
                    VehicleEngineData data= parser.parse(VehicleEngineData.class,byteBuf);
                    vehicleCommonData.setVehicleEngineData(data);
                    break;
                }
                case 5: {
                    //2.5、车辆位置数据
                    VehiclePositionData data= parser.parse(VehiclePositionData.class,byteBuf);
                    vehicleCommonData.setVehiclePositionData(data);
                    break;
                }
                case 6: {
                    //2.6、极值数据
                    VehicleLimitValueData data= parser.parse(VehicleLimitValueData.class,byteBuf);
                    vehicleCommonData.setVehicleLimitValueData(data);
                    break;
                }
                case 7: {
                    //2.7、报警数据
                    VehicleAlarmData vehicleAlarmData= parser.parse(VehicleAlarmData.class,byteBuf);
                    vehicleCommonData.setVehicleAlarmData(vehicleAlarmData);
                    break;
                }
                case 8:{
                    //2.8、可充电储能装置电压数据
                    VehicleStorageVoltageData vehicleStorageVoltageData= parser.parse(VehicleStorageVoltageData.class,byteBuf);
                    vehicleCommonData.setVehicleStorageVoltageData(vehicleStorageVoltageData);
                    break;
                }
                case 9:{
                    //2.9、可充电储能装置温度数据
                    VehicleStorageTemperatureData vehicleStorageTemperatureData= parser.parse(VehicleStorageTemperatureData.class,byteBuf);
                    vehicleCommonData.setVehicleStorageTemperatureData(vehicleStorageTemperatureData);
                    break;
                }
                default:{
                    logger.warn("Parse Vehicle Common Data Interrupted,Unknown Flag["+flag+"]");
                    //2.8、如果是自定义数据,只做展现,不解析
                    //2.8.1、解析长度
                    int dataLen= byteBuf.readUnsignedShort();
                    //2.8.2、获取接下来的报文
                    byte[] content=new byte[dataLen];
                    byteBuf.getBytes(0,content);
                    break A;
//                      throw BaseRuntimeException.getException("Parse Vehicle Data Failed,Unknown Flag["+flag+"]");
                }
            }
        }
        return vehicleCommonData;
    }

    @Override
    public void deProcess(VehicleCommonData data, ByteBuf result, FieldDeProcessContext processContext) {
        if(data.getVehicleBaseData()!=null){
            result.writeByte(1);
            result.writeBytes(parser.toByteBuf(data.getVehicleBaseData()));
        }
        if(data.getVehicleMotorData()!=null){
            result.writeByte(2);
            result.writeBytes(parser.toByteBuf(data.getVehicleMotorData()));
        }
        if(data.getVehicleFuelBatteryData()!=null){
            result.writeByte(3);
            result.writeBytes(parser.toByteBuf(data.getVehicleFuelBatteryData()));
        }
        if(data.getVehicleEngineData()!=null){
            result.writeByte(4);
            result.writeBytes(parser.toByteBuf(data.getVehicleEngineData()));
        }
        if(data.getVehiclePositionData()!=null){
            result.writeByte(5);
            result.writeBytes(parser.toByteBuf(data.getVehiclePositionData()));
        }
        if(data.getVehicleLimitValueData()!=null){
            result.writeByte(6);
            result.writeBytes(parser.toByteBuf(data.getVehicleLimitValueData()));
        }
        if(data.getVehicleAlarmData()!=null){
            result.writeByte(7);
            result.writeBytes(parser.toByteBuf(data.getVehicleAlarmData()));
        }
        if(data.getVehicleStorageVoltageData()!=null){
            result.writeByte(8);
            result.writeBytes(parser.toByteBuf(data.getVehicleStorageVoltageData()));
        }
        if(data.getVehicleStorageTemperatureData()!=null){
            result.writeByte(9);
            result.writeBytes(parser.toByteBuf(data.getVehicleStorageTemperatureData()));
        }
    }
}

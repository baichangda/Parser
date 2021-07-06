package com.bcd.parser.impl.gb32960.processor;

import com.bcd.parser.impl.gb32960.data.*;
import com.bcd.parser.processer.FieldDeProcessContext;
import com.bcd.parser.processer.FieldProcessContext;
import com.bcd.parser.processer.FieldProcessor;
import io.netty.buffer.ByteBuf;


public class PacketDataFieldProcessor extends FieldProcessor<PacketData> {

    public PacketDataFieldProcessor() {
    }

    public PacketData parse(ByteBuf data, int flag, FieldProcessContext processContext){
        PacketData packetData=null;
        switch (flag){
            //车辆登入
            case 1:{
                packetData= parser.parse(VehicleLoginData.class, data,processContext);
                break;
            }
            //车辆实时信息
            case 2:{
                packetData= parser.parse(VehicleRealData.class,data,processContext);
                break;
            }
            //补发信息上报
            case 3:{
                packetData= parser.parse(VehicleSupplementData.class,data,processContext);
                break;
            }
            //车辆登出
            case 4:{
                packetData= parser.parse(VehicleLogoutData.class,data,processContext);
                break;
            }
            //平台登入
            case 5:{
                packetData= parser.parse(PlatformLoginData.class,data,processContext);
                break;
            }
            //平台登出
            case 6:{
                packetData= parser.parse(PlatformLogoutData.class,data,processContext);
                break;
            }
            //心跳
            case 7:{
                break;
            }
            //终端校时
            case 8:{
                break;
            }
        }
        return packetData;
    }

    @Override
    public PacketData process(ByteBuf data, FieldProcessContext processContext) {
        Packet packet=(Packet)processContext.instance;
        return parse(data,packet.getFlag(),processContext);
    }

    @Override
    public void deProcess(PacketData data, ByteBuf dest, FieldDeProcessContext processContext) {
        int flag=((Packet)processContext.instance).getFlag();
        switch (flag){
            //车辆登入
            case 1:{
                parser.deParse(data,dest,processContext);
                break;
            }
            //车辆实时信息
            case 2:{
                parser.deParse(data,dest,processContext);
                break;
            }
            //补发信息上报
            case 3:{
                parser.deParse(data,dest,processContext);
                break;
            }
            //车辆登出
            case 4:{
                parser.deParse(data,dest,processContext);
                break;
            }
            //平台登入
            case 5:{
                parser.deParse(data,dest,processContext);
                break;
            }
            //平台登出
            case 6:{
                parser.deParse(data,dest,processContext);
                break;
            }
            //心跳
            case 7:{
                break;
            }
            //终端校时
            case 8:{
                break;
            }
        }
    }
}

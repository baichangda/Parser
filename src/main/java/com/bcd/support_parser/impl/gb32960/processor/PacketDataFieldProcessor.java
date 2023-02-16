package com.bcd.support_parser.impl.gb32960.processor;

import com.bcd.support_parser.impl.gb32960.data.*;
import com.bcd.support_parser.processor.Processor;
import com.bcd.support_parser.processor.ProcessContext;
import io.netty.buffer.ByteBuf;


public class PacketDataFieldProcessor extends Processor<PacketData> {

    @Override
    public PacketData process(final ByteBuf data, final ProcessContext parentContext) {
        Packet packet=(Packet)parentContext.instance;
        PacketData packetData=null;
        switch (packet.flag) {
            //车辆登入
            case 1 : {
                packetData = parser.parse(VehicleLoginData.class, data, parentContext);
                break;
            }

            //车辆实时信息
            case 2 : {
                packetData = parser.parse(VehicleRealData.class, data, parentContext);
                break;
            }

            //补发信息上报
            case 3 : {
                packetData = parser.parse(VehicleSupplementData.class, data, parentContext);
                break;
            }

            //车辆登出
            case 4 : {
                packetData = parser.parse(VehicleLogoutData.class, data, parentContext);
                break;
            }

            //平台登入
            case 5 : {
                packetData = parser.parse(PlatformLoginData.class, data, parentContext);
                break;
            }

            //平台登出
            case 6 : {
                packetData = parser.parse(PlatformLogoutData.class, data, parentContext);
                break;
            }

            //心跳
            case 7 : {
                break;
            }

            //终端校时
            case 8 : {
                break;
            }
        }
        return packetData;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, PacketData instance) {
        parser.deParse(instance, data, parentContext);
    }
}

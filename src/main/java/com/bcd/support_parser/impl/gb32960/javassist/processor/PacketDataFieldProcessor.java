package com.bcd.support_parser.impl.gb32960.javassist.processor;

import com.bcd.support_parser.impl.gb32960.data.*;
import com.bcd.support_parser.javassist.Parser;
import com.bcd.support_parser.javassist.processor.FieldProcessor;
import com.bcd.support_parser.javassist.processor.FieldProcessContext;
import io.netty.buffer.ByteBuf;


public class PacketDataFieldProcessor extends FieldProcessor<PacketData> {

    @Override
    public PacketData process(final ByteBuf data, final FieldProcessContext context) {
        Packet packet=(Packet)context.instance;
        final Parser parser = context.parser;
        PacketData packetData=null;
        switch (packet.flag) {
            //车辆登入
            case 1 : {
                packetData = parser.parse(VehicleLoginData.class, data, context);
                break;
            }

            //车辆实时信息
            case 2 : {
                packetData = parser.parse(VehicleRealData.class, data, context);
                break;
            }

            //补发信息上报
            case 3 : {
                packetData = parser.parse(VehicleSupplementData.class, data, context);
                break;
            }

            //车辆登出
            case 4 : {
                packetData = parser.parse(VehicleLogoutData.class, data, context);
                break;
            }

            //平台登入
            case 5 : {
                packetData = parser.parse(PlatformLoginData.class, data, context);
                break;
            }

            //平台登出
            case 6 : {
                packetData = parser.parse(PlatformLogoutData.class, data, context);
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



}

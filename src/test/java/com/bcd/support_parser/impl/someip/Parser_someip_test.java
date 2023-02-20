package com.bcd.support_parser.impl.someip;

import com.bcd.support_parser.Parser;
import com.bcd.support_parser.impl.someip.data.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

public class Parser_someip_test {
    @Test
    public void test(){
        Parser.withDefaultLogCollector();
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
        String data = "000100e4000000ac0009000a0304000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a1";
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        final Packet packet = Parser.parse(Packet.class, byteBuf, null);
        ByteBuf dest=Unpooled.buffer();
        Parser.deParse(packet, dest,null);
        System.out.println(data);
        System.out.println(ByteBufUtil.hexDump(dest));
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }
}

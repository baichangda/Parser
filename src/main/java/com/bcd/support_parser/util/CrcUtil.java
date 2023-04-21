package com.bcd.support_parser.util;

import io.netty.buffer.ByteBuf;

public class CrcUtil {
    public static long crc32_mpeg_2(ByteBuf byteBuf, int offset, int length) {
        long wCRCin = 0xFFFFFFFFL;
        for (int i = offset, cnt = offset + length; i < cnt; i++) {
            for (int j = 0; j < 8; j++) {
                boolean bit = ((byteBuf.getByte(i) >> (7 - j) & 1) == 1);
                boolean c31 = ((wCRCin >> 31 & 1) == 1);
                wCRCin <<= 1;
                if (c31 ^ bit) {
                    wCRCin ^= 0x04C11DB7L;
                }
            }
        }
        wCRCin &= 0xFFFFFFFFL;
        return wCRCin;
    }
}

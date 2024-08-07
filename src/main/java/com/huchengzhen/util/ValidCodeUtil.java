package com.huchengzhen.util;

import io.netty.buffer.ByteBuf;

public class ValidCodeUtil {
    static public byte validCode(ByteBuf byteBuf) {
        var ref = new Object() {
            byte v = 0;
        };
        byteBuf.forEachByte(b -> {
            ref.v ^= b;
            return true;
        });

        return ref.v;
    }

    static public byte validCode(byte[] bytes) {
        byte v = 0;
        for (byte b : bytes) {
            v ^= b;
        }

        return v;
    }
}

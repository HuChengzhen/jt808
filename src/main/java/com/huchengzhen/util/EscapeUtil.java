package com.huchengzhen.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EscapeUtil {

    static public ByteBuf escape(ByteBuf buf) {
        ByteBuf to = Unpooled.buffer();
        buf.forEachByte(value -> {
            if (value == 0x7e) {
                to.writeByte(0x7d);
                to.writeByte(0x02);
            } else if (value == 0x7d) {
                to.writeByte(0x7d);
                to.writeByte(0x01);
            } else {
                to.writeByte(value);
            }
            return true;
        });
        return to;
    }
}

package com.huchengzhen.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

@Data
public class Response implements ToByteBuf {
    private int sequenceNumber;
    private int id;
    private byte result;

    public ByteBuf toByteBuf() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeShort(sequenceNumber);
        buffer.writeShort(id);
        buffer.writeByte(result);
        return buffer;
    }
}

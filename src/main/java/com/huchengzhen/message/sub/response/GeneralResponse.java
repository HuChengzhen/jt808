package com.huchengzhen.message.sub.response;

import com.huchengzhen.message.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

@Data
public class GeneralResponse implements Response {
    private int sequenceNumber;
    private int id;
    private byte result;

    @Override
    public int geMessageId() {
        return 0x8001;
    }

    public ByteBuf toByteBuf() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeShort(sequenceNumber);
        buffer.writeShort(id);
        buffer.writeByte(result);
        return buffer;
    }
}

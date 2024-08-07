package com.huchengzhen.message;


import com.huchengzhen.util.EscapeUtil;
import com.huchengzhen.util.ValidCodeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

@Data
public class Message {
    private MessageHeader header;
    private ToByteBuf body;

    public ByteBuf toByteBuf() {
        ByteBuf beforeEscape = Unpooled.buffer();
        ByteBuf headerByteBuf = header.toByteBuf();
        beforeEscape.writeBytes(headerByteBuf);
        beforeEscape.writeBytes(body.toByteBuf());
        beforeEscape.writeByte(ValidCodeUtil.validCode(beforeEscape));


        ByteBuf escaped = EscapeUtil.escape(beforeEscape);

        ByteBuf msgByteBuf = Unpooled.buffer();
        msgByteBuf.writeByte(0x7e);
        msgByteBuf.writeBytes(escaped);
        msgByteBuf.writeByte(0x7e);

        return msgByteBuf;
    }
}

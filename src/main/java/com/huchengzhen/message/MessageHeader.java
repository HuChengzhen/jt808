package com.huchengzhen.message;

import com.huchengzhen.util.BCD;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class MessageHeader {
    private int id;
    private MessageBodyProperty property;
    private byte[] number;
    private int sequenceNumber;
    private String numberString;

    static public MessageHeader fromByteBuf(ByteBuf buf) {
        MessageHeader header = new MessageHeader();
        header.id = buf.readUnsignedShort();
        header.property = MessageBodyProperty.fromByteBuf(buf.readBytes(2));
        header.number = new byte[6];
        buf.readBytes(header.number);
        header.sequenceNumber = buf.readUnsignedShort();

        header.numberString = BCD.BCDtoString(header.number);
        return header;
    }

    public ByteBuf toByteBuf() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeShort(id);
        ByteBuf propertyByteBuf = property.toByteBuf();
        buffer.writeBytes(propertyByteBuf);
        buffer.writeBytes(number);
        buffer.writeShort(sequenceNumber);
        return buffer;
    }
}

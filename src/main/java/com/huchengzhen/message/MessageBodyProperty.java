package com.huchengzhen.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Arrays;

@ToString
@Data
public class MessageBodyProperty {
    public static int ignoreByteLength = 6;
    BigInteger length;

    public static MessageBodyProperty fromByteBuf(ByteBuf buf) {
        MessageBodyProperty mb = new MessageBodyProperty();
        byte[] dst = new byte[2];
        buf.getBytes(0, dst);
        dst[0] &= 0b00000011;
        mb.length = new BigInteger(dst);
        return mb;
    }

    public ByteBuf toByteBuf() {
        if (length == null || length.equals(BigInteger.ZERO)) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeShort(0);
            return buffer;
        }

        byte[] byteArray = length.toByteArray();
        if (byteArray.length < 2) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeByte(0);
            buffer.writeByte(byteArray[0]);
            return buffer;
        }
        return Unpooled.copiedBuffer(Arrays.copyOfRange(byteArray, byteArray.length - 2, byteArray.length));
    }
}

package com.huchengzhen.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


public class MessageEscapeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        ByteBuf to = Unpooled.buffer();
        for (; ; ) {
            int between = byteBuf.bytesBefore((byte) 0x7d);
            if (between > 0) {
                to.writeBytes(byteBuf.readBytes(between));
                byte escape = byteBuf.readByte();
                if (escape == 0x02) {
                    to.writeByte(0x7e);
                } else {
                    to.writeByte(0x7d);
                }
            } else {
                break;
            }
        }

        to.writeBytes(byteBuf);

        list.add(to);
    }
}

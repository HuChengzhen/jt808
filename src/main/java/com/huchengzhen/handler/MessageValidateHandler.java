package com.huchengzhen.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


public class MessageValidateHandler extends ByteToMessageDecoder {



    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes < 1) {
            channelHandlerContext.channel().close();
            return;
        }
        int beforeValidate = readableBytes - 1;
        byte[] bytes = new byte[beforeValidate];
        byteBuf.getBytes(0, bytes);
        byte v = 0;
        for (byte b : bytes) {
            v ^= b;
        }

        if (byteBuf.getByte(readableBytes - 1) != v) {
            channelHandlerContext.channel().close();
        }


        list.add(byteBuf.readBytes(byteBuf.readableBytes() - 1));
        byteBuf.readByte();
    }
}

package com.huchengzhen.handler;

import com.huchengzhen.Context;
import com.huchengzhen.message.sub.response.GeneralResponse;
import com.huchengzhen.message.MessageHeader;
import com.huchengzhen.message.sub.PositionMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<ByteBuf> {

    static public MessageHandler instance = new MessageHandler();

    static public ConcurrentHashMap<String, Context> NUMBER_TO_CONTEXT = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        ByteBuf header = byteBuf.readBytes(12);
        MessageHeader messageHeader = MessageHeader.fromByteBuf(header);

        ByteBuf body = byteBuf.readBytes(byteBuf.readableBytes());


        System.out.println(messageHeader);
        System.out.println(body);

        String numberString = messageHeader.getNumberString();
        NUMBER_TO_CONTEXT.compute(numberString, (s, c) -> {
            if (c == null) {
                c = new Context(ctx.channel(), messageHeader.getNumber(), messageHeader.getProperty());
            } else {
                if (!c.getChannel().get().equals(ctx.channel())) {
                    c.getChannel().set(ctx.channel());
                }
            }
            c.getId().set(messageHeader.getId());
            c.getRequestSequenceNumber().set(messageHeader.getSequenceNumber());
            return c;
        });

        if (messageHeader.getId() == 0x200) {
            PositionMessage positionMessage = PositionMessage.fromByteBuf(body);
            System.out.println(positionMessage);
            System.out.println(positionMessage.getTimeString());
            System.out.println(positionMessage.isAcc());
            System.out.println(positionMessage.isPosition());
            System.out.println(Arrays.toString(positionMessage.ewsn()));
        }

        Context context = MessageHandler.NUMBER_TO_CONTEXT.get(numberString);
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setId(context.getId().get());
        generalResponse.setSequenceNumber(context.getRequestSequenceNumber().get());
        generalResponse.setResult((byte) 0);
        context.send(generalResponse);
    }
}

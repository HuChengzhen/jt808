package com.huchengzhen.handler;

import com.huchengzhen.Context;
import com.huchengzhen.message.Message;
import com.huchengzhen.message.MessageHeader;
import com.huchengzhen.message.Response;
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

        if (context != null && context.getChannel().get().isActive()) {
            Response response = new Response();
            response.setId(context.getId().get());
            response.setSequenceNumber(context.getRequestSequenceNumber().get());
            response.setResult((byte) 0);

            Message message = new Message();
            MessageHeader responseHeader = new MessageHeader();
            responseHeader.setId(0x8001);
            responseHeader.setNumber(context.getNumber());
            responseHeader.setProperty(context.getProperty());
            // 递增
            responseHeader.setSequenceNumber(context.getSequenceNumber());

            message.setHeader(responseHeader);
            message.setBody(response);
            context.getChannel().get().writeAndFlush(message);
        }
    }
}

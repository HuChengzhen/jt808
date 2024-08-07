package com.huchengzhen.util;

import com.huchengzhen.Context;
import com.huchengzhen.handler.MessageHandler;
import lombok.extern.java.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log
public class RemoveUnActiveChannelUtil {
    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    static {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            removeUnActiveChannel(MessageHandler.NUMBER_TO_CONTEXT);
        }, 5, 5, TimeUnit.SECONDS);

//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            MessageHandler.NUMBER_TO_CONTEXT.forEach((s, context) -> {
//                if (context.getChannel().get().isActive()) {
//                    Response response = new Response();
//                    response.setId(context.getId().get());
//                    response.setSequenceNumber(context.getRequestSequenceNumber().get());
//                    response.setResult((byte) 0);
//
//                    Message message = new Message();
//                    MessageHeader responseHeader = new MessageHeader();
//                    responseHeader.setId(0x8001);
//                    responseHeader.setNumber(context.getNumber());
//                    responseHeader.setProperty(context.getProperty());
//                    // 递增
//                    responseHeader.setSequenceNumber(context.getSequenceNumber());
//
//                    message.setHeader(responseHeader);
//                    message.setBody(response);
//                    context.getChannel().get().writeAndFlush(message);
//                }
//            });
//        }, 5, 5, TimeUnit.SECONDS);
    }

    public static void removeUnActiveChannel(ConcurrentHashMap<String, Context> numberToChanel) {
        log.info("remove Un Active Channel");
        if (numberToChanel == null) {
            return;
        }
        numberToChanel.replaceAll((s, context) -> {
            if (context.getChannel().get().isActive()) {
                return context;
            } else {
                log.info("removed");
                return null;
            }
        });
    }
}

package com.huchengzhen;

import com.huchengzhen.message.MessageBodyProperty;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ToString
@Getter

public class Context {

    public Context(Channel channel, byte[] number, MessageBodyProperty property) {
        this.getChannel().set(channel);
        this.number = number;
        this.property = property;
    }

    private final AtomicInteger id = new AtomicInteger();
    private final AtomicReference<Channel> channel = new AtomicReference<>();

    private byte[] number;
    private MessageBodyProperty property;
    private final AtomicInteger requestSequenceNumber = new AtomicInteger(0);
    private final AtomicInteger sequenceNumber = new AtomicInteger(0);

    public int getSequenceNumber() {
        return sequenceNumber.getAndIncrement();
    }
}

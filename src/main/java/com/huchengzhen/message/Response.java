package com.huchengzhen.message;

import io.netty.buffer.ByteBuf;

public interface Response {
    int geMessageId();

    ByteBuf toByteBuf();
}

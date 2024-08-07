package com.huchengzhen;

import com.huchengzhen.handler.MessageHandler;
import com.huchengzhen.handler.MessageValidateHandler;
import com.huchengzhen.handler.MessageWrapEncoder;
import com.huchengzhen.util.RemoveUnActiveChannelUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.java.Log;

@Log
public class Main {
    public static void main(String[] args) {
        RemoveUnActiveChannelUtil removeUnActiveChannelUtil = new RemoveUnActiveChannelUtil();
        System.out.println(removeUnActiveChannelUtil);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DelimiterBasedFrameDecoder(99999, true, Unpooled.wrappedBuffer(new byte[]{Constant.IDENTIFICATION_BIT})));
                            pipeline.addLast(new MessageValidateHandler());
                            pipeline.addLast(MessageHandler.instance);
                            pipeline.addLast(new MessageWrapEncoder());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(8080).sync();
            log.info("start");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
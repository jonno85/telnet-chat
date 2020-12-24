package com.jfilippini.telnetchat.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class SimpleChatServerHandler extends ChannelInboundHandlerAdapter {

    private static final String CR = "\n\r";

    /**
     * thread safe collection to holds all the connections
     */
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
        String remoteAddress = ctx.channel().remoteAddress().toString();

        ctx.writeAndFlush("Your remote address is " + remoteAddress + CR);
        group.writeAndFlush(ctx.channel().remoteAddress() + " > Go online" + CR);
        log.info("qui e {} channel {}", group.isEmpty(), ctx.channel());
        group.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel channel = ctx.channel();
        group.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush(channel.remoteAddress() + ": " + msg + CR);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Assert.notNull(ctx, "[Assertion failed] - ChannelHandlerContext is required; it must not be null");

        Channel channel = ctx.channel();
        group.writeAndFlush(channel.remoteAddress() + " > Offline, current online population:" + group.size() + CR);
    }
}

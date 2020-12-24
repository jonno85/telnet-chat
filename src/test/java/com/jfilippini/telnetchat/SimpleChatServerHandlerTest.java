package com.jfilippini.telnetchat;

import com.jfilippini.telnetchat.netty.handler.SimpleChatServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketAddress;

import static org.mockito.Mockito.*;

public class SimpleChatServerHandlerTest {

    private SimpleChatServerHandler somethingServerHandler;

    private ChannelHandlerContext channelHandlerContext;

    private Channel channel;

    private SocketAddress remoteAddress;

    ChannelGroup group;

    @Before
    public void setUp() {
        somethingServerHandler = new SimpleChatServerHandler();
        channelHandlerContext = mock(ChannelHandlerContext.class);
        channel = spy(new EmbeddedChannel());
        group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        remoteAddress = mock(SocketAddress.class);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testChannelActive() {
        when(channelHandlerContext.channel()).thenReturn(channel);
        when(channelHandlerContext.channel().remoteAddress()).thenReturn(remoteAddress);
        somethingServerHandler.channelActive(channelHandlerContext);
    }

    @Test
    public void testChannelRead() {
        when(channelHandlerContext.channel()).thenReturn(channel);
        somethingServerHandler.channelRead(channelHandlerContext, "test message");
    }

    @Test
    public void testChannelInactive() {
        when(channelHandlerContext.channel()).thenReturn(channel);
        somethingServerHandler.channelInactive(channelHandlerContext);
    }

    @Test
    public void testExceptionCaught() {

    }
}
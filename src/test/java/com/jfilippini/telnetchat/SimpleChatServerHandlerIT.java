package com.jfilippini.telnetchat;

import com.jfilippini.telnetchat.netty.handler.SimpleChatServerHandler;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.*;
import java.util.Queue;

public class SimpleChatServerHandlerIT {

    private final static String TEXT_MESSAGE = "first message";
    private final static String CR = "\n\r";
    private final static String CHANNEL_NAME = "embedded";
    private final static String DEFAULT_REPLY = "Your remote address is ";
    private static final String GOES_ONLINE = " > Go online";

    @Test
    @DisplayName("message auto reply")
    public void testMessaageAutoReply() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new SimpleChatServerHandler());

        embeddedChannel.writeInbound(TEXT_MESSAGE);

        Queue<Object> outboundMessages = embeddedChannel.outboundMessages();

        Assertions.assertThat(outboundMessages.poll()).isEqualTo(DEFAULT_REPLY + CHANNEL_NAME + CR);
        embeddedChannel.close();
    }

    @Test
    @DisplayName("test goes online message")
    public void testMessageGoesOnline() {
        ChannelInboundHandler channelInboundHandler = new SimpleChatServerHandler();
        EmbeddedChannel embeddedChannel1 = new EmbeddedChannel(channelInboundHandler);
        EmbeddedChannel embeddedChannel2 = new EmbeddedChannel(channelInboundHandler);

        embeddedChannel1.writeInbound(TEXT_MESSAGE);
        embeddedChannel2.readInbound();

        Queue<Object> outboundMessages1 = embeddedChannel1.outboundMessages();
        Queue<Object> outboundMessages2 = embeddedChannel2.outboundMessages();

        Assertions.assertThat(outboundMessages2.poll()).isEqualTo(DEFAULT_REPLY + CHANNEL_NAME + CR);
        Assertions.assertThat(outboundMessages1.poll()).isEqualTo(DEFAULT_REPLY + CHANNEL_NAME + CR);
        Assertions.assertThat(outboundMessages1.poll()).isEqualTo(CHANNEL_NAME + GOES_ONLINE + CR);

        embeddedChannel1.close();
        embeddedChannel2.close();
    }
}
package com.jfilippini.telnetchat.config;

import com.jfilippini.telnetchat.netty.handler.SimpleChatChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfiguration {

    private final NettyProperties nettyProperties;

    @Bean(name = "serverBootstrap")
    @Qualifier("serverBootstrap")
    public ServerBootstrap bootstrap(SimpleChatChannelInitializer simpleChatChannelInitializer) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(simpleChatChannelInitializer);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog());
        return serverBootstrap;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(nettyProperties.getBossCount());
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(nettyProperties.getWorkerCount());
    }

    @Bean
    public InetSocketAddress tcpSocketAddress() {
        return new InetSocketAddress(nettyProperties.getTcpPort());
    }
}

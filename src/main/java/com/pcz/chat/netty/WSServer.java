package com.pcz.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author picongzhi
 */
@Slf4j
@Component
public class WSServer {
    private EventLoopGroup mainEventLoopGroup;
    private EventLoopGroup subEventLoopGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    public WSServer() {
        mainEventLoopGroup = new NioEventLoopGroup();
        subEventLoopGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainEventLoopGroup, subEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }

    public void start() {
        channelFuture = serverBootstrap.bind(8088);
        log.info("netty websocket server started!");
    }

    private static class SingletonWSServer {
        static final WSServer INSTANCE = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletonWSServer.INSTANCE;
    }
}

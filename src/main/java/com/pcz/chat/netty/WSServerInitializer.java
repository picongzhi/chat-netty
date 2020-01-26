package com.pcz.chat.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author picongzhi
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        // 支持http协议
        // http编解码器
        channelPipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        channelPipeline.addLast(new ChunkedWriteHandler());
        // 对HttpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // 支持websocket协议
        // websocket服务器处理的协议，用于指定给客户端连接访问的路由 /ws，处理一些繁重的复杂的事
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        channelPipeline.addLast(new ChatHandler());
    }
}

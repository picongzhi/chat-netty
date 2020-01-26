package com.pcz.chat.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author picongzhi
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup clientChannelGroups = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接收到的数据" + content);

        for (Channel channel : clientChannelGroups) {
            channel.writeAndFlush(new TextWebSocketFrame("from server at " +
                    LocalDateTime.now() + ", message is " + content));
        }
    }

    /**
     * 当客户端连接服务端之后，获取客户端的channel，并且放到channelGroup中进行管理
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext context) throws Exception {
        super.handlerAdded(context);
        clientChannelGroups.add(context.channel());
    }

    /**
     * 当客户端断开连接后，移除客户端channel
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) throws Exception {
        super.handlerRemoved(context);
        clientChannelGroups.remove(context.channel());
        System.out.println("客户端断开连接，channel对应的长id: " + context.channel().id().asLongText());
        System.out.println("客户端断开连接: channel对应的短id" + context.channel().id().asShortText());
    }
}

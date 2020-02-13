package com.pcz.chat.netty;

import com.pcz.chat.enums.MessageAction;
import com.pcz.chat.service.UserService;
import com.pcz.chat.utils.JsonUtil;
import com.pcz.chat.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author picongzhi
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup userChannelGroups = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame msg) throws Exception {
        Channel channel = context.channel();

        // 获取客户端传输过来的消息
        String content = msg.text();
        DataContent dataContent = JsonUtil.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();
        if (MessageAction.CONNECT.type.equals(action)) {
            String senderId = dataContent.getChatMessage().getSenderId();
            UserChannelManager.put(senderId, channel);

            // 测试连接
            userChannelGroups.forEach(channelGroup -> log.info(channelGroup.id().asLongText()));
            UserChannelManager.output();
        } else if (MessageAction.CHAT.type.equals(action)) {
            ChatMessage chatMessage = dataContent.getChatMessage();
            String message = chatMessage.getMsg();
            String senderId = chatMessage.getSenderId();
            String receiverId = chatMessage.getReceiverId();

            UserService userService = SpringUtil.getBean(UserService.class);
            String messageId = userService.saveMessage(chatMessage);
            chatMessage.setMsgId(messageId);

            Channel receiverChannel = UserChannelManager.get(receiverId);
            if (receiverChannel == null) {
                // 消息推送
            } else {
                if (userChannelGroups.find(receiverChannel.id()) != null) {
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.objectToJson(chatMessage)));
                } else {
                    // 消息推送
                }
            }
        } else if (MessageAction.SIGNED.type.equals(action)) {
            UserService userService = SpringUtil.getBean(UserService.class);
            String extend = dataContent.getExtend();
            String[] msgIds = extend.split(",");

            List<String> msgIdList = new ArrayList<>();
            for (String msgId : msgIds) {
                if (StringUtils.isNotBlank(msgId)) {
                    msgIdList.add(msgId);
                }
            }

            if (CollectionUtils.isNotEmpty(msgIdList)) {
                userService.updateMessageSigned(msgIdList);
            }
        } else if (MessageAction.KEEPALIVE.type.equals(action)) {

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
        userChannelGroups.add(context.channel());
    }

    /**
     * 当客户端断开连接后，移除客户端channel
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) throws Exception {
        userChannelGroups.remove(context.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        userChannelGroups.remove(ctx.channel());
    }
}

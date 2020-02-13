package com.pcz.chat.enums;

/**
 * @author picongzhi
 */

public enum MessageAction {
    /**
     * 第一次(或重连)初始化连接
     */
    CONNECT(1, "第一次(或重连)初始化连接"),
    /**
     * 聊天消息
     */
    CHAT(2, "聊天消息"),
    /**
     * 消息签收
     */
    SIGNED(3, "消息签收"),
    /**
     * 客户端保持心跳
     */
    KEEPALIVE(4, "客户端保持心跳");

    public final Integer type;
    public final String content;

    MessageAction(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}

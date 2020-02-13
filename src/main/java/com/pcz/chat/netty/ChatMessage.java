package com.pcz.chat.netty;

import lombok.Data;

/**
 * @author picongzhi
 */
@Data
public class ChatMessage {
    private String msgId;
    private String msg;
    private String senderId;
    private String receiverId;
}

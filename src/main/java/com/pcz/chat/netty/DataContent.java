package com.pcz.chat.netty;

import lombok.Data;

/**
 * @author picongzhi
 */
@Data
public class DataContent {
    private Integer action;
    private ChatMessage chatMessage;
    private String extend;
}

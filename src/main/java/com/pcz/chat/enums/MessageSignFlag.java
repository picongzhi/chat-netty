package com.pcz.chat.enums;

/**
 * @author picongzhi
 */
public enum MessageSignFlag {
    /**
     * 未签收
     */
    UNSIGNED(0, "未签收"),
    /**
     * 已签收
     */
    SIGNED(1, "已签收");

    public final Integer type;
    public final String content;

    MessageSignFlag(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}

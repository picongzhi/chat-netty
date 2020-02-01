package com.pcz.chat.enums;

/**
 * @author picongzhi
 */
public enum FriendRequestOperationType {
    /**
     * 忽略
     */
    IGNORE(0, "忽略"),
    /**
     * 通过
     */
    ACCEPT(1, "通过");

    private Integer type;
    private String message;

    FriendRequestOperationType(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

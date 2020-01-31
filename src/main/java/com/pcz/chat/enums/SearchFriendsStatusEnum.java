package com.pcz.chat.enums;


/**
 * @author picongzhi
 */
public enum SearchFriendsStatusEnum {
    /**
     * 正常
     */
    SUCCESS(0, "OK"),
    /**
     * 无此用户
     */
    USER_NOT_EXIST(1, "无此用户"),
    /**
     * 不能添加你自己
     */
    NOT_YOURSELF(2, "不能添加你自己"),
    /**
     * 该用户已经是你的好友
     */
    ALREADY_FRIENDS(3, "该用户已经是你的好友");

    private Integer status;
    private String message;

    SearchFriendsStatusEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

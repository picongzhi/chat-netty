package com.pcz.chat.pojo;

import lombok.Data;

/**
 * @author picongzhi
 */
@Data
public class FriendRequestUser {
    private String sendUserId;
    private String sendUsername;
    private String sendNickname;
    private String sendFaceImage;
}

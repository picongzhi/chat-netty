package com.pcz.chat.vo;

import lombok.Data;

/**
 * @author picongzhi
 */
@Data
public class FriendRequestUserVo {
    private String sendUserId;
    private String sendUsername;
    private String sendNickname;
    private String sendFaceImage;
}

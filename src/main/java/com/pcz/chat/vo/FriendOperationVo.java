package com.pcz.chat.vo;

import com.pcz.chat.enums.FriendRequestOperationType;
import lombok.Data;

/**
 * @author picongzhi
 */
@Data
public class FriendOperationVo {
    private String sendUserId;
    private String acceptUserId;
    private FriendRequestOperationType type;
}

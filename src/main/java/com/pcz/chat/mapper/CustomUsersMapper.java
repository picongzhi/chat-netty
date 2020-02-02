package com.pcz.chat.mapper;

import com.pcz.chat.pojo.FriendInfo;
import com.pcz.chat.pojo.FriendRequestUser;

import java.util.List;

/**
 * @author picongzhi
 */
public interface CustomUsersMapper {
    /**
     * 根据接收用户查询好友请求
     *
     * @param acceptUserId 接收用户id
     * @return List<FriendsRequest>
     */
    List<FriendRequestUser> queryFriendRequestList(String acceptUserId);

    /**
     * 查询好友
     *
     * @param userId 用户id
     * @return List<FriendInfo>
     */
    List<FriendInfo> queryFriends(String userId);
}

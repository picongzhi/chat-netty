package com.pcz.chat.mapper;

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
}

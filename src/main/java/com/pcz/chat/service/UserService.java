package com.pcz.chat.service;

import com.pcz.chat.enums.SearchFriendsStatus;
import com.pcz.chat.pojo.Users;
import com.pcz.chat.vo.FriendInfoVo;
import com.pcz.chat.vo.FriendOperationVo;
import com.pcz.chat.vo.FriendRequestUserVo;

import java.util.List;

/**
 * @author picongzhi
 */
public interface UserService {
    /**
     * 根据用户名判断用户是否存在
     *
     * @param username 用户名
     * @return 用户是否存在
     */
    boolean isUserExist(String username);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return Users
     */
    Users login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return Users
     */
    Users register(Users user);

    /**
     * 更新用户
     *
     * @param user Users
     * @return Users
     */
    Users updateUserInfo(Users user);

    /**
     * 搜索朋友前置条件
     *
     * @param myUserId       自己的用户id
     * @param friendUsername 好友用户名
     * @return SearchFriendsStatus
     */
    SearchFriendsStatus searchFriendsPrecondition(String myUserId, String friendUsername);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return Users
     */
    Users queryUserByUsername(String username);

    /**
     * 发送好友请求
     *
     * @param myUserId       自己的用户id
     * @param friendUsername 好友用户名
     */
    void addFriendRequest(String myUserId, String friendUsername);

    /**
     * 获取接收用户的好友请求
     *
     * @param acceptUserId 接收用户id
     * @return List<FriendRequestUserVo>
     */
    List<FriendRequestUserVo> queryFriendRequests(String acceptUserId);

    /**
     * 处理好友请求
     *
     * @param friendOperationVo FriendOperationVo
     */
    void handleFriendRequest(FriendOperationVo friendOperationVo);

    /**
     * 根据userId获取好友
     *
     * @param userId 用户id
     * @return List<FriendInfoVo>
     */
    List<FriendInfoVo> getFriends(String userId);
}

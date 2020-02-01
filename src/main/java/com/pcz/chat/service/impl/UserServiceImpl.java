package com.pcz.chat.service.impl;

import com.pcz.chat.enums.FriendRequestOperationType;
import com.pcz.chat.enums.SearchFriendsStatus;
import com.pcz.chat.mapper.CustomUsersMapper;
import com.pcz.chat.mapper.FriendsRequestMapper;
import com.pcz.chat.mapper.MyFriendsMapper;
import com.pcz.chat.mapper.UsersMapper;
import com.pcz.chat.pojo.FriendRequestUser;
import com.pcz.chat.pojo.FriendsRequest;
import com.pcz.chat.pojo.MyFriends;
import com.pcz.chat.pojo.Users;
import com.pcz.chat.service.UserService;
import com.pcz.chat.utils.FastDFSClient;
import com.pcz.chat.utils.FileUtil;
import com.pcz.chat.utils.MD5Util;
import com.pcz.chat.utils.QRCodeUtil;
import com.pcz.chat.vo.FriendOperationVo;
import com.pcz.chat.vo.FriendRequestUserVo;
import com.pcz.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CustomUsersMapper customUsersMapper;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);

        return result != null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users login(String username, String password) {
        Example example = new Example(Users.class);
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("password", MD5Util.getMD5String(password));

        return usersMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users register(Users user) {
        String id = sid.nextShort();
        user.setId(id);
        user.setNickname(user.getUsername());
        user.setFaceImage("");
        user.setFaceImageBig("");
        user.setPassword(MD5Util.getMD5String(user.getPassword()));

        String qrCodeFilePath = "/Users/picongzhi/chat/user/" + user.getId() + "_qrcode.png";
        QRCodeUtil.createQRCode(qrCodeFilePath, "chat_qrcode:" + user.getUsername());
        MultipartFile multipartFile = FileUtil.fileToMultipart(qrCodeFilePath);

        String qrCodePath = "";
        try {
            qrCodePath = fastDFSClient.uploadQRCode(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setQrcode(qrCodePath);

        usersMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);

        return queryUserById(user.getId());
    }

    private Users queryUserById(String id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public SearchFriendsStatus searchFriendsPrecondition(String myUserId, String friendUsername) {
        Users user = queryUserByUsername(friendUsername);
        if (user == null) {
            return SearchFriendsStatus.USER_NOT_EXIST;
        }

        if (user.getId().equals(myUserId)) {
            return SearchFriendsStatus.NOT_YOURSELF;
        }

        Example myFriendsExample = new Example(MyFriends.class);
        myFriendsExample.createCriteria()
                .andEqualTo("myUserId", myUserId)
                .andEqualTo("myFriendUserId", user.getId());
        MyFriends myFriend = myFriendsMapper.selectOneByExample(myFriendsExample);
        if (myFriend != null) {
            return SearchFriendsStatus.ALREADY_FRIENDS;
        }

        return SearchFriendsStatus.SUCCESS;
    }

    @Override
    public Users queryUserByUsername(String username) {
        Example example = new Example(Users.class);
        example.createCriteria().andEqualTo("username", username);

        return usersMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addFriendRequest(String myUserId, String friendUsername) {
        Users user = queryUserByUsername(friendUsername);

        Example example = new Example(FriendsRequest.class);
        example.createCriteria()
                .andEqualTo("sendUserId", myUserId)
                .andEqualTo("acceptUserId", user.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(example);
        if (friendsRequest != null) {
            return;
        }

        String id = sid.nextShort();
        friendsRequest = new FriendsRequest();
        friendsRequest.setId(id);
        friendsRequest.setSendUserId(myUserId);
        friendsRequest.setAcceptUserId(user.getId());
        friendsRequest.setRequestDateTime(new Date());

        friendsRequestMapper.insert(friendsRequest);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendRequestUserVo> queryFriendRequests(String acceptUserId) {
        List<FriendRequestUser> friendRequestUsers = customUsersMapper.queryFriendRequestList(acceptUserId);
        if (CollectionUtils.isEmpty(friendRequestUsers)) {
            return null;
        }

        List<FriendRequestUserVo> friendRequestUserVos = new ArrayList<>();
        friendRequestUsers.forEach(friendRequestUser -> {
            FriendRequestUserVo friendRequestUserVo = new FriendRequestUserVo();
            BeanUtils.copyProperties(friendRequestUser, friendRequestUserVo);
            friendRequestUserVos.add(friendRequestUserVo);
        });

        return friendRequestUserVos;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void handleFriendRequest(FriendOperationVo friendOperationVo) {
        deleteFriendRequest(friendOperationVo.getSendUserId(), friendOperationVo.getAcceptUserId());
        if (friendOperationVo.getType().equals(FriendRequestOperationType.ACCEPT)) {
            saveFriend(friendOperationVo.getSendUserId(), friendOperationVo.getAcceptUserId());
            saveFriend(friendOperationVo.getAcceptUserId(), friendOperationVo.getSendUserId());
        }
    }

    private void deleteFriendRequest(String sendUserId, String acceptUserId) {
        Example example = new Example(FriendsRequest.class);
        example.createCriteria()
                .andEqualTo("sendUserId", sendUserId)
                .andEqualTo("acceptUserId", acceptUserId);
        friendsRequestMapper.deleteByExample(example);
    }

    private void saveFriend(String sendUserId, String acceptUserId) {
        MyFriends myFriends = new MyFriends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyUserId(acceptUserId);
        myFriends.setMyFriendUserId(sendUserId);

        myFriendsMapper.insert(myFriends);
    }
}

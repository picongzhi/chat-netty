package com.pcz.chat.service.impl;

import com.pcz.chat.mapper.UsersMapper;
import com.pcz.chat.pojo.Users;
import com.pcz.chat.service.UserService;
import com.pcz.chat.utils.FastDFSClient;
import com.pcz.chat.utils.FileUtil;
import com.pcz.chat.utils.MD5Util;
import com.pcz.chat.utils.QRCodeUtil;
import com.pcz.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

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
        QRCodeUtil.createQRCode(qrCodeFilePath, "chat_qrcode:" + user.getId());
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
}

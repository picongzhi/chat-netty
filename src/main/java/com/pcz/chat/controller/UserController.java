package com.pcz.chat.controller;

import com.pcz.chat.bo.UserBo;
import com.pcz.chat.common.Result;
import com.pcz.chat.pojo.Users;
import com.pcz.chat.service.UserService;
import com.pcz.chat.utils.FastDFSClient;
import com.pcz.chat.utils.FileUtil;
import com.pcz.chat.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author picongzhi
 */
@RestController
@RequestMapping("/u")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @PostMapping("/registerOrLogin")
    public Result registerOrLogin(@RequestBody Users user) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return Result.errorMessage("用户名或密码不能为空");
        }

        boolean exist = userService.isUserExist(user.getUsername());
        Users result = null;
        if (exist) {
            result = userService.login(user.getUsername(), user.getPassword());
            if (result == null) {
                return Result.errorMessage("用户名或密码不正确");
            }
        } else {
            result = userService.register(user);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(result, userVo);

        return Result.ok(userVo);
    }

    @PostMapping("/uploadFaceBase64")
    public Result uploadFaceBase64(@RequestBody UserBo userBo) throws Exception {
        String base64Data = userBo.getFaceData();
        String userFacePath = "/Users/picongzhi/chat/tmp" + userBo.getUserId() + "userFace64.png";
        FileUtil.base64ToFile(userFacePath, base64Data);

        MultipartFile multipartFile = FileUtil.fileToMultipart(userFacePath);
        String path = fastDFSClient.uploadBase64(multipartFile);
        String[] pathArr = path.split("\\.");
        String thumbImageUrl = pathArr[0] + "_80x80." + pathArr[1];

        Users users = new Users();
        users.setId(userBo.getUserId());
        users.setFaceImageBig(path);
        users.setFaceImage(thumbImageUrl);
        userService.updateUserInfo(users);

        return Result.ok(users);
    }
}

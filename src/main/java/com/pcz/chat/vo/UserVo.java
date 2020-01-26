package com.pcz.chat.vo;

import lombok.Data;

@Data
public class UserVo {
    private String id;
    private String username;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String qrcode;
}
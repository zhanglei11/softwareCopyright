package com.sursoft.vision.system.vo;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LoginVO {
    private String accessToken;
    private Long expiresIn;
    private UserInfoVO userInfo;

    @Data
    @Builder
    public static class UserInfoVO {
        private Long userId;
        private String realName;
        private List<String> roles;
        private List<String> perms;
    }
}

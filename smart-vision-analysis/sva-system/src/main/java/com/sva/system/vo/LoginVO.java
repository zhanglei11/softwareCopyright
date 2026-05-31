package com.sva.system.vo;
import lombok.Data;
import java.util.List;
@Data
public class LoginVO {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UserInfoVO userInfo;

    @Data
    public static class UserInfoVO {
        private Long id;
        private String username;
        private String realName;
        private List<String> roles;
    }
}

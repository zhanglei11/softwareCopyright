package com.sursoft.vision.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class UserEditDTO {
    @NotBlank @Size(min=2, max=20)
    private String realName;
    @NotBlank @Pattern(regexp="^1[3-9]\\d{9}$", message="手机号格式不正确")
    private String phone;
    @Size(max=50)
    private String department;
    @NotEmpty
    private List<Long> roleIds;
    @NotNull
    private Integer status;
}

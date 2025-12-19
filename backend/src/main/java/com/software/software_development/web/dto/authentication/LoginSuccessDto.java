package com.software.software_development.web.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSuccessDto {
    private String accessToken;

    private String refreshToken;
}

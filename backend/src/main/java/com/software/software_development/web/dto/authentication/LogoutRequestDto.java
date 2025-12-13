package com.software.software_development.web.dto.authentication;

import com.software.software_development.core.security.enums.ClientType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDto {
    private String email;

    private ClientType clientType;
}

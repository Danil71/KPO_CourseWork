package com.software.software_development.core.utility;

import java.util.Set;

import org.springframework.security.core.userdetails.User;

import com.software.software_development.model.entity.UserEntity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {
    public static User convert(UserEntity user) {
        return new User(user.getEmail(), user.getPassword(), Set.of(user.getRole()));
    }
}

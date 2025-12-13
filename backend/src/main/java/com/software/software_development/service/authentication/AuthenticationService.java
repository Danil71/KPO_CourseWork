package com.software.software_development.service.authentication;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.cache.LoadingCache;
import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.error.InvalidOtpException;
import com.software.software_development.core.security.enums.ClientType;
import com.software.software_development.core.utility.JwtUtils;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.repository.UserRepository;
import com.software.software_development.service.email.EmailService;
import com.software.software_development.web.dto.authentication.LoginRequestDto;
import com.software.software_development.web.dto.authentication.LoginSuccessDto;
import com.software.software_development.web.dto.authentication.OtpVerificationRequestDto;
import com.software.software_development.web.dto.email.EmailRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoadingCache<String, Integer> oneTimePasswordCache;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public String login(LoginRequestDto userLoginRequestDto) {
        final UserEntity user = userRepository.findByEmailIgnoreCase(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid user email: " + userLoginRequestDto.getEmail()));
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid user password: " + userLoginRequestDto.getPassword());
        }
        sendOtp(user, Constants.OTP_EMAIL_SUBJECT);
        return "Одноразовый пароль успешно отправлен на Вашу электронную почту. Пожалуйста, подтвердите его";
    }

    public LoginSuccessDto verifyOtp(OtpVerificationRequestDto otpVerificationRequestDto, ClientType clientType) {
        UserEntity user = userRepository.findByEmailIgnoreCase(otpVerificationRequestDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid user email: " + otpVerificationRequestDto.getEmail()));

        Integer storedOneTimePassword;
        try {
            storedOneTimePassword = oneTimePasswordCache.get(user.getEmail());
        } catch (ExecutionException e) {
            throw new IllegalStateException("Failed to retrieve OTP from cache due to an internal system error for user " + user.getEmail());
        }

        if (!storedOneTimePassword.equals(otpVerificationRequestDto.getOneTimePassword())) {
           throw new InvalidOtpException("Invalid OTP password: " + otpVerificationRequestDto.getOneTimePassword());
        }

        return generateLoginSuccessDto(user, clientType);
    }

    public LoginSuccessDto refreshToken(final String refreshToken, ClientType clientType) {
        if (refreshToken == null) {
            throw new CredentialsExpiredException("Refresh token is missing");
        }
        if (jwtUtils.isTokenExpired(refreshToken)) {
            throw new CredentialsExpiredException("Refresh token has expired");
        }
        final var user = userRepository.findByEmailIgnoreCase(jwtUtils.extractEmail(refreshToken))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials for refresh token"));
        refreshTokenService.validateAndGetToken(refreshToken, clientType);
        return generateLoginSuccessDto(user, clientType);
    }

    public void sendOtp(final UserEntity user, final String subject) {
        oneTimePasswordCache.invalidate(user.getEmail());

        final var otp = new Random().ints(1, 100000, 999999).sum();
        oneTimePasswordCache.put(user.getEmail(), otp);

        EmailRequestDto emailRequest = new EmailRequestDto();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject(subject);
        emailRequest.setMessage("Ваш одноразовый пароль: " + otp + ". (время действия ограничено 10 минутами)");

        CompletableFuture.runAsync(() -> emailService.sendSimpleEmailAsync(emailRequest));
    }

    public void invalidateOtp(final String email) {
        oneTimePasswordCache.invalidate(email);
    }

    public void logout(String email, ClientType clientType) {
        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid user email: " + email));
        refreshTokenService.deleteToken(user, clientType);
    }

    private LoginSuccessDto generateLoginSuccessDto(UserEntity user, ClientType clientType) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null when generating tokens");
        }
        String refreshToken = jwtUtils.generateRefreshToken(user);

        refreshTokenService.replaceToken(user, refreshToken, clientType);

        LoginSuccessDto dto = new LoginSuccessDto();
        dto.setAccessToken(jwtUtils.generateAccessToken(user));
        dto.setRefreshToken(refreshToken);
        return dto;
    }
}

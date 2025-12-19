package com.software.software_development.web.controller.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.security.enums.ClientType;
import com.software.software_development.service.authentication.AuthenticationService;
import com.software.software_development.service.entity.UserService;
import com.software.software_development.web.dto.authentication.LoginRequestDto;
import com.software.software_development.web.dto.authentication.LoginSuccessDto;
import com.software.software_development.web.dto.authentication.LogoutRequestDto;
import com.software.software_development.web.dto.authentication.OtpRequestDto;
import com.software.software_development.web.dto.authentication.OtpVerificationRequestDto;
import com.software.software_development.web.dto.authentication.RefreshTokenRequestDto;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.AUTH_URL)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping(value = Constants.LOGIN_URL)
    public ResponseEntity<String> userLoginHandler(@RequestBody @Valid final LoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok(authenticationService.login(userLoginRequestDto));
    }

    @PostMapping(value = "/send-otp")
    public ResponseEntity<Void> sendOtpHandler(@RequestBody @Valid final OtpRequestDto otpRequestDto) {
        authenticationService.sendOtp(userService.getByEmail(otpRequestDto.getEmail()), Constants.OTP_EMAIL_SUBJECT);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/invalidate-otp")
    public ResponseEntity<Void> invalidateOtp(@RequestBody @Valid final OtpRequestDto otpRequestDto) {
        authenticationService.invalidateOtp(otpRequestDto.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/verify-otp")
    public ResponseEntity<LoginSuccessDto> otpVerificationHandler(@RequestBody @Valid final OtpVerificationRequestDto otpVerificationRequestDto,
                                                  HttpServletResponse response) {
        LoginSuccessDto loginSuccessDto = authenticationService.verifyOtp(otpVerificationRequestDto, ClientType.WEB);

        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(loginSuccessDto.getRefreshToken());
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        loginSuccessDto.setRefreshToken(null);
        return ResponseEntity.ok(loginSuccessDto);
    }

    @PostMapping(value = "/verify-otp-direct")
    public LoginSuccessDto otpVerificationDirectHandler(@RequestBody @Valid final OtpVerificationRequestDto otpVerificationRequestDto) {
        return authenticationService.verifyOtp(otpVerificationRequestDto, ClientType.DESKTOP);
    }

    @PutMapping(value = "/refresh-token")
    public ResponseEntity<LoginSuccessDto> tokenRefresherHandler(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                                 HttpServletResponse response) {
        LoginSuccessDto loginSuccessDto = authenticationService.refreshToken(refreshToken, ClientType.WEB);

        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(loginSuccessDto.getRefreshToken());
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        loginSuccessDto.setRefreshToken(null);
        return ResponseEntity.ok(loginSuccessDto);
    }

    @PutMapping(value = "/refresh-token-direct")
    public LoginSuccessDto tokenRefresherDirectHandler(@RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        return authenticationService.refreshToken(refreshTokenRequestDto.getRefreshToken(), ClientType.DESKTOP);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logoutHandler(@RequestBody @Valid LogoutRequestDto logoutRequestDto, HttpServletResponse response) {
        authenticationService.logout(logoutRequestDto.getEmail(), ClientType.WEB);

        ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path(Constants.API_URL + Constants.AUTH_URL)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/logout-direct")
    public ResponseEntity<Void> logoutHandler(@RequestBody @Valid LogoutRequestDto logoutRequestDto) {
        authenticationService.logout(logoutRequestDto.getEmail(), logoutRequestDto.getClientType());
        return ResponseEntity.noContent().build();
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path(Constants.API_URL + Constants.AUTH_URL)
                .maxAge(30 * 24 * 60 * 60)
                .build();
    }
}

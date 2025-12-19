package com.software.software_development.web.controller.email;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.service.email.EmailService;
import com.software.software_development.web.dto.email.EmailRequestDto;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + "/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping(value = "/simple", consumes = "application/json")
    public ResponseEntity<String> sendSimpleEmail(
            @RequestBody @Valid EmailRequestDto emailRequestDto) {
        emailService.sendSimpleEmail(emailRequestDto);
        return ResponseEntity.ok("Ваше письмо успешно отправлено!");
    }

    @PostMapping(value = "/attachments", consumes = "multipart/form-data")
    public ResponseEntity<String> sendEmailWithAttachments(
            @RequestPart("emailRequest") @Valid EmailRequestDto emailRequestDto,
            @RequestPart("attachments") List<MultipartFile> attachments) throws MessagingException {
        emailService.sendEmailWithAttachments(emailRequestDto, attachments);
        return ResponseEntity.ok("Ваше письмо успешно отправлено!");
    }
}

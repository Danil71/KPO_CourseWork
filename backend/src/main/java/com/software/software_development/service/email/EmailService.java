package com.software.software_development.service.email;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.software.software_development.web.dto.email.EmailRequestDto;

import jakarta.mail.MessagingException;

@Service
public interface EmailService {
    void sendSimpleEmail(EmailRequestDto emailRequestDto);

    void sendSimpleEmailAsync(EmailRequestDto emailRequestDto);

    void sendEmailWithAttachments(EmailRequestDto emailRequestDto, List<MultipartFile> attachments) throws MessagingException;
}

package com.software.software_development.web.controller.report;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.service.report.SoftwareReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + "/reports")
@RequiredArgsConstructor
public class SoftwareReportController{

    private final SoftwareReportService softwareReportService;

    @GetMapping
    public ResponseEntity<byte[]> exportDetailedSoftwareExcelReport() throws IOException{

        byte[] excelBytes = softwareReportService.generateDetailedSoftwareExcelReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "software_report.xlsx");
        headers.setContentLength(excelBytes.length);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        
    }
}

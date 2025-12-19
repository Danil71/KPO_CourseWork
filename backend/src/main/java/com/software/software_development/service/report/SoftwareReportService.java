package com.software.software_development.service.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.SoftwareEntity;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.repository.SoftwareRepository;
import com.software.software_development.web.dto.report.SoftwareTaskDepartmentReportDto;
import com.software.software_development.web.dto.report.TaskReportDetailsDto;

@Service
public class SoftwareReportService {

    private final SoftwareRepository softwareRepository;

    public SoftwareReportService(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }
    
    @Transactional(readOnly = true)
    public byte[] generateDetailedSoftwareExcelReport() throws IOException {
        
        List<SoftwareTaskDepartmentReportDto> softwaresForReport = softwareRepository.findAllWithTasksAndDepartments().stream()
                .map(this::mapToSoftwareTaskDepartmentReportDto)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("softwares", softwaresForReport);


        try (InputStream templateStream = getClass().getResourceAsStream("/software_report_template.xlsx")) {

            if (templateStream == null) {
                throw new RuntimeException("Excel template not found: detailed_software_report_template.xlsx");
            }

            byte[] excelBytes = JxlsPoiTemplateFillerBuilder.newInstance()
                    .withTemplate(templateStream)
                    .buildAndFill(data);
            return excelBytes;

        } catch (Exception e) {
            System.err.println("Error generating Excel report: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to generate Excel report", e);
        }
    }

    private SoftwareTaskDepartmentReportDto mapToSoftwareTaskDepartmentReportDto(SoftwareEntity software) {
        List<TaskReportDetailsDto> taskDetails = new ArrayList<>();
        if (software.getTasks() != null && !software.getTasks().isEmpty()) {
            taskDetails = software.getTasks().stream()
                    .map(this::mapToTaskReportDetailsDto)
                    .toList();
        }

        return new SoftwareTaskDepartmentReportDto(
                software.getId(),
                software.getName(),
                software.getDescription(),
                software.getStartDate(),
                software.getEndDate(),
                taskDetails
        );
    }

    private TaskReportDetailsDto mapToTaskReportDetailsDto(TaskEntity task) {

        Set<String> departmentNames = null;
        Set<Integer> departmentEfficiency = null;

        if (task.getDepartments() != null && !task.getDepartments().isEmpty()) {
            departmentNames = task.getDepartments().stream()
                    .map(DepartmentEntity::getName)
                    .collect(Collectors.toSet());
            departmentEfficiency = task.getDepartments().stream()
                    .map(DepartmentEntity::getEfficiency)
                    .collect(Collectors.toSet());
        }

        return new TaskReportDetailsDto(
                task.getId(),
                task.getDescription(),
                task.getDifficulty(),
                task.getStartDate(),
                task.getEndDate(),
                task.getHours(),
                departmentNames,
                departmentEfficiency
        );
    }
}

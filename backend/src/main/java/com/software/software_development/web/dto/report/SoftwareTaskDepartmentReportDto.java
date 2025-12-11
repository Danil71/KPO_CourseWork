package com.software.software_development.web.dto.report;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class SoftwareTaskDepartmentReportDto {
    private Long softwareId;
    private String softwareName;
    private String softwareDescription;
    private Date softwareStartDate;
    private Date softwareEndDate;

    private String taskSummaries;

    private List<TaskReportDetailsDto> taskDetails;

    public SoftwareTaskDepartmentReportDto(
            Long softwareId, String softwareName, String softwareDescription,
            Date softwareStartDate, Date softwareEndDate, List<TaskReportDetailsDto> taskDetails) {
        this.softwareId = softwareId;
        this.softwareName = softwareName;
        this.softwareDescription = softwareDescription;
        this.softwareStartDate = softwareStartDate;
        this.softwareEndDate = softwareEndDate;
        this.taskDetails = taskDetails;

        if (taskDetails != null && !taskDetails.isEmpty()) {
            this.taskSummaries = taskDetails.stream()
                    .map(td -> String.format("Задача #%d (%s), Сложность: %d, Часы: %.1f, Департаменты: [%s]",
                            td.getTaskId(), td.getTaskDescription(), td.getTaskDifficulty(), td.getTaskHours(), td.getDepartmentNames()))
                    .collect(Collectors.joining(";\n"));
        } else {
            this.taskSummaries = "Нет задач";
        }
    }
}
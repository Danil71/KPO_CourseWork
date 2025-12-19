package com.software.software_development.web.dto.report;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class TaskReportDetailsDto {
    private Long taskId;
    private String taskDescription;
    private int taskDifficulty;
    private Date taskStartDate;
    private Date taskEndDate;
    private float taskHours;
    private String departmentNames;
    private String departmentEfficiency;

    public TaskReportDetailsDto(Long taskId, String taskDescription, int taskDifficulty,
                                Date taskStartDate, Date taskEndDate, float taskHours,
                                Set<String> departmentNames, Set<Integer> departmentEfficiency) {
        this.taskId = taskId;
        this.taskDescription = taskDescription;
        this.taskDifficulty = taskDifficulty;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskHours = taskHours;
        
        this.departmentNames = departmentNames != null && !departmentNames.isEmpty()
                               ? String.join(", ", departmentNames)
                               : "N/A";
        
        this.departmentEfficiency = departmentEfficiency != null && !departmentEfficiency.isEmpty()
                                  ? departmentEfficiency.stream().map(String::valueOf).collect(Collectors.joining(", "))
                                  : "N/A";
    }
    

}

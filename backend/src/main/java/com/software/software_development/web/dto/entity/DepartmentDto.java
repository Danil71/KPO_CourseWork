package com.software.software_development.web.dto.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.software_development.core.configuration.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(
            regexp = Constants.NAME_PATTERN,
            message = "Name must be 1-20 characters long and contain only letters and hyphens"
    )
    private String name;

    @NotNull
    private String specialty;

    @NotNull
    private String efficiency;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> taskIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<TaskDto> tasks;

}
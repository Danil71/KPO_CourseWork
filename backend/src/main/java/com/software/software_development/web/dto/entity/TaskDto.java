package com.software.software_development.web.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.validation.IsoDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(
            regexp = Constants.NAME_PATTERN,
            message = "Name must be 1-20 characters long and contain only letters and hyphens"
    )
    private String description;

    @NotNull
    private int difficulty;

    @NotBlank
    @IsoDate
    private String startDate;

    @NotBlank
    @IsoDate
    private String endDate;

    @NotNull
    private float hours;

    @NotNull
    @Min(1)
    private Long softwareId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String softwareName;

}

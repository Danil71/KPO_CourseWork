package com.software.software_development.web.controller.entity;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.utility.Formatter;
import com.software.software_development.model.entity.SoftwareEntity;
import com.software.software_development.model.enums.SoftwareSortType;
import com.software.software_development.service.entity.SoftwareService;
import com.software.software_development.web.dto.entity.SoftwareDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.ADMIN_PREFIX + "/software")
@RequiredArgsConstructor
public class SoftwareController {
    private final SoftwareService softwareService;

    private final ModelMapper modelMapper;
    public static final String SOFTWARE_URL = "/filter";

    public SoftwareDto toDto(SoftwareEntity entity) {
        return modelMapper.map(entity, SoftwareDto.class);
    }

    public SoftwareEntity toEntity(SoftwareDto dto) {
        return modelMapper.map(dto, SoftwareEntity.class);
    }

    @GetMapping
    public PageDto<SoftwareDto> getAllByFilters(
            @RequestParam(name = "startDateFrom", required = false) String startDateFromStr,
            @RequestParam(name = "startDateTo", required = false) String startDateToStr,
            @RequestParam(name = "endDateFrom", required = false) String endDateFromStr,
            @RequestParam(name = "endDateTo", required = false) String endDateToStr,
            @RequestParam(name = "taskIds", required = false) List<Long> taskIds,
            @RequestParam(name = "searchInfo", required = false) String searchInfo,
            @RequestParam(name = "sortType", defaultValue = "NAME_DESC") SoftwareSortType sortType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) { 

        Date startDateFrom = startDateFromStr != null ? Formatter.parse(startDateFromStr) : null;
        Date startDateTo = startDateToStr != null ? Formatter.parse(startDateToStr) : null;
        Date endDateFrom = endDateFromStr != null ? Formatter.parse(endDateFromStr) : null;
        Date endDateTo = endDateToStr != null ? Formatter.parse(endDateToStr) : null;

        return PageDtoMapper.toDto(softwareService.getAllByFilters(
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                taskIds,
                searchInfo,
                sortType,
                page,
                size
        ), this::toDto);
    }   

    @GetMapping("/{id}")
    public SoftwareDto get(@PathVariable(name = "id") Long id) {
        return toDto(softwareService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SoftwareDto create(@RequestBody @Valid SoftwareDto dto) {
        return toDto(softwareService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public SoftwareDto update(@PathVariable(name = "id") Long id, @RequestBody SoftwareDto dto) {
        return toDto(softwareService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public SoftwareDto delete(@PathVariable(name = "id") Long id) {
        return toDto(softwareService.delete(id));
    }
}

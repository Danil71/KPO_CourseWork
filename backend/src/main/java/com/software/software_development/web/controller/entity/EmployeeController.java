package com.software.software_development.web.controller.entity;

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
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.service.entity.EmployeeService;
import com.software.software_development.web.dto.entity.EmployeeDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + "/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    public EmployeeDto toDto(EmployeeEntity entity) {
        EmployeeDto dto = modelMapper.map(entity, EmployeeDto.class);
        dto.setDepartmentId(entity.getDepartment().getId());
        dto.setDepartmentName(entity.getDepartment().getName());
        return dto;
    }

    public EmployeeEntity toEntity(EmployeeDto dto) {
        EmployeeEntity entity = modelMapper.map(dto, EmployeeEntity.class);
        entity.setDepartment(departmentService.get(dto.getDepartmentId()));
        return entity;
    }

    @GetMapping
    public PageDto<EmployeeDto> getAll(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(employeeService.getAll(name, page, size), this::toDto);
    }

    @GetMapping("/{id}")
    public EmployeeDto get(@PathVariable Long id) {
        return toDto(employeeService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto create(@RequestBody @Valid EmployeeDto dto) {
        return toDto(employeeService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        return toDto(employeeService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public EmployeeDto delete(@PathVariable Long id) {
        return toDto(employeeService.delete(id));
    }
}

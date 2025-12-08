package com.software.software_development.web.controller.entity;

import java.util.HashSet;
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
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.service.entity.TaskService;
import com.software.software_development.web.dto.entity.DepartmentDto;
import com.software.software_development.web.dto.entity.TaskDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + "/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    public DepartmentDto toDto(DepartmentEntity entity) {
        DepartmentDto dto = modelMapper.map(entity, DepartmentDto.class);
        dto.setTasks(
                entity.getTasks()
                        .stream()
                        .map(task -> modelMapper.map(task, TaskDto.class))
                        .toList()
        );
        return dto;
    }

    public DepartmentEntity toEntity(DepartmentDto dto) {
        DepartmentEntity entity = modelMapper.map(dto, DepartmentEntity.class);
        List<TaskEntity> tasks = taskService.getByIds(dto.getTaskIds());
        entity.setTasks(new HashSet<>(tasks));
        return entity;
    }

    @GetMapping
    public List<DepartmentDto> getAll(
            @RequestParam(name = "name", defaultValue = "") String name) {
        return departmentService.getAll(name).stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public DepartmentDto get(@PathVariable(name = "id") Long id) {
        return toDto(departmentService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto create(@RequestBody @Valid DepartmentDto dto) {
        return toDto(departmentService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public DepartmentDto update(@PathVariable(name = "id") Long id, @RequestBody DepartmentDto dto) {
        return toDto(departmentService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public DepartmentDto delete(@PathVariable(name = "id") Long id) {
        return toDto(departmentService.delete(id));
    }
}

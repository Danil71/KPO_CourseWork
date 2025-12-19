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
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.service.entity.SoftwareService;
import com.software.software_development.service.entity.TaskService;
import com.software.software_development.web.dto.entity.TaskDto;
import com.software.software_development.web.dto.pagination.PageDto;
import com.software.software_development.web.dto.pagination.PageDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.ADMIN_PREFIX + "/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final SoftwareService softwareService;
    private final ModelMapper modelMapper;

    public TaskDto toDto(TaskEntity entity) {
        TaskDto dto = modelMapper.map(entity, TaskDto.class);
        dto.setSoftwareId(entity.getSoftware().getId());
        dto.setSoftwareName(entity.getSoftware().getName());
        return dto;
    }

    public TaskEntity toEntity(TaskDto dto) {
        TaskEntity entity = modelMapper.map(dto, TaskEntity.class);
        entity.setSoftware(softwareService.get(dto.getSoftwareId()));
        return entity;
    }

    @GetMapping
    public PageDto<TaskDto> getAll(
            @RequestParam(name = "description", defaultValue = "") String description,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        return PageDtoMapper.toDto(taskService.getAll(description, page, size), this::toDto);
    }

    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return toDto(taskService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody @Valid TaskDto dto) {
        return toDto(taskService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable Long id, @RequestBody @Valid TaskDto dto) {
        return toDto(taskService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public TaskDto delete(@PathVariable Long id) {
        return toDto(taskService.delete(id));
    }
}

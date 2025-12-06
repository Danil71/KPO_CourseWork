package com.software.software_development.service.entity;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService extends AbstractEntityService<TaskEntity> {
    private final TaskRepository repository;

    @Transactional(readOnly = true)
    public List<TaskEntity> getAll(String description) {
        if (description == null || description.isBlank()) {
            return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        }
        return repository.findByDescriptionContainingIgnoreCase(description);
    }

   @Transactional(readOnly = true)
    public Page<TaskEntity> getAll(String description, int page, int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        if (description == null || description.isBlank()) {
            return repository.findAll(pageRequest);
        }
        return repository.findByNameContainingIgnoreCase(description, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<TaskEntity> getByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return StreamSupport.stream(repository.findAllById(ids).spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public TaskEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(TaskEntity.class, id));
    }

    @Transactional
    public TaskEntity create(TaskEntity entity) {
        validate(entity, null);
        return repository.save(entity);
    }

    @Transactional
    public TaskEntity update(long id, TaskEntity entity) {
        validate(entity, null);
        final TaskEntity existsEntity = get(id);
        existsEntity.setDescription(entity.getDescription());
        existsEntity.setDifficulty(entity.getDifficulty());
        existsEntity.setStartDate(entity.getStartDate());
        existsEntity.setEndDate(entity.getEndDate());
        existsEntity.setDepartments(entity.getDepartments());
        existsEntity.setSoftware(entity.getSoftware());
        existsEntity.setHours(entity.getHours());
        return repository.save(existsEntity);
    }

    @Transactional
    public TaskEntity delete(long id) {
        final TaskEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    @Override
    protected void validate(TaskEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Task entity is null");
        }
        validateStringField(entity.getDescription(), "Task description");
        if (entity.getDifficulty() < 1 || entity.getDifficulty() > 10) {
            throw new IllegalArgumentException("Task difficulty must be between 1 and 10");
        }
    }
}

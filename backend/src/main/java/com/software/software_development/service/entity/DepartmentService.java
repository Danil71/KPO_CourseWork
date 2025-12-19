package com.software.software_development.service.entity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService extends AbstractEntityService<DepartmentEntity> {
    private final DepartmentRepository repository;

    @Transactional(readOnly = true)
    public List<DepartmentEntity> getAll(String name) {
        if (name == null || name.isBlank()) {
            return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        }
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public DepartmentEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(DepartmentEntity.class, id));
    }

    @Transactional
    public DepartmentEntity create(DepartmentEntity entity) {
        validate(entity, null);
        DepartmentEntity department = new DepartmentEntity(entity.getName(), entity.getSpecialty(), entity.getEfficiency());
        for (TaskEntity taskEntity : entity.getTasks()) {
            department.addTask(taskEntity);
        }
        return repository.save(department);
    }

    @Transactional
    public DepartmentEntity update(long id, DepartmentEntity entity) {
        validate(entity, id);
        final DepartmentEntity existsEntity = get(id);
        existsEntity.setName(entity.getName());
        existsEntity.setSpecialty(entity.getSpecialty());
        existsEntity.setEfficiency(entity.getEfficiency());
        syncTasks(existsEntity, entity.getTasks());
        return repository.save(existsEntity);
    }

    @Transactional
    public DepartmentEntity delete(long id) {
        final DepartmentEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    @Override
    protected void validate(DepartmentEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Department entity is null");
        }
        validateStringField(entity.getName(), "Department name");

        final Optional<DepartmentEntity> existingEntity = repository.findByNameIgnoreCase(entity.getName());
        if (existingEntity.isPresent() && !existingEntity.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                    String.format("Department with name %s already exists", entity.getName())
            );
        }
    }

    private void syncTasks(DepartmentEntity existsEntity, Set<TaskEntity> updatedTasks) {
        Set<TaskEntity> tasksToRemove = existsEntity.getTasks().stream()
                .filter(task -> !updatedTasks.contains(task))
                .collect(Collectors.toSet());
        for (TaskEntity task : tasksToRemove) {
            existsEntity.deleteTask(task);
        }

        for (TaskEntity task : updatedTasks) {
            if (!existsEntity.getTasks().contains(task)) {
                existsEntity.addTask(task);
            }
        }
    }
}

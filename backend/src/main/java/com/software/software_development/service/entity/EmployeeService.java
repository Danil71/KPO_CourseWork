package com.software.software_development.service.entity;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService extends AbstractEntityService<EmployeeEntity>  {
    private final EmployeeRepository repository;


    @Transactional(readOnly = true)
    public List<EmployeeEntity> getAll(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        }
        return repository.findByNameContainingIgnoreCase(fullName);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeEntity> getAll(String name, int page, int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        if (name == null || name.isBlank()) {
            return repository.findAll(pageRequest);
        }
        return repository.findByNameContainingIgnoreCase(name, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<EmployeeEntity> getByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return StreamSupport.stream(repository.findAllById(ids).spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(EmployeeEntity.class, id));
    }

    @Transactional
    public EmployeeEntity create(EmployeeEntity entity) {
        validate(entity, null);
        return repository.save(entity);
    }

    @Transactional
    public EmployeeEntity update(long id, EmployeeEntity entity) {
        validate(entity, null);
        final EmployeeEntity existsEntity = get(id);
        existsEntity.setName(entity.getName());
        existsEntity.setDepartment(entity.getDepartment());
        return repository.save(existsEntity);
    }

    @Transactional
    public EmployeeEntity delete(long id) {
        final EmployeeEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    @Override
    protected void validate(EmployeeEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Employee entity is null");
        }
        validateStringField(entity.getName(), "Employee name");
        if (!entity.getName().matches(Constants.NAME_PATTERN)) {
            throw new IllegalArgumentException("Employee name has invalid format: " + entity.getName());
        }
    }
}

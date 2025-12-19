package com.software.software_development.service.entity;

import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.SoftwareEntity;
import com.software.software_development.model.enums.SoftwareSortType;
import com.software.software_development.repository.SoftwareRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoftwareService extends AbstractEntityService<SoftwareEntity> {

    private final SoftwareRepository repository;

    @Transactional(readOnly = true)
    public List<SoftwareEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<SoftwareEntity> getAll(String name, int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        if (name == null || name.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional(readOnly = true)
    public SoftwareEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(SoftwareEntity.class, id));
    }

    @Transactional
    public SoftwareEntity create(SoftwareEntity entity) {
        validate(entity, null);
        return repository.save(entity);
    }

    @Transactional
    public SoftwareEntity update(long id, SoftwareEntity entity) {
        validate(entity, id);
        SoftwareEntity existing = get(id);
        existing.setName(entity.getName());
        existing.setDescription(entity.getDescription());
        existing.setStartDate(entity.getStartDate());
        existing.setEndDate(entity.getEndDate());
        return repository.save(existing);
    }

    @Transactional
    public SoftwareEntity delete(long id) {
        SoftwareEntity entity = get(id);
        repository.delete(entity);
        return entity;
    }

     @Transactional(readOnly = true)
    public Page<SoftwareEntity> getAllByFilters(
            Date startDateFrom,
            Date startDateTo,
            Date endDateFrom,
            Date endDateTo,
            List<Long> taskIds,
            String searchInfo,
            SoftwareSortType sortType,
            int page,
            int size
    ) {
        Sort sort = Sort.by(getSortInfo(sortType));
        Pageable pageRequest = PageRequest.of(page, size, sort);

        return repository.findByFilters(
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                nullIfEmpty(taskIds),
                (searchInfo != null && !searchInfo.isBlank()) ? searchInfo.toLowerCase() : null,
                pageRequest
        );
    }

    private Order getSortInfo(SoftwareSortType sortType) {
        if (sortType == null) {

            return new Order(Sort.Direction.DESC, "name");
        }
        return switch (sortType) {
            case NAME_ASC -> new Order(Sort.Direction.ASC, "name");
            case NAME_DESC -> new Order(Sort.Direction.DESC, "name");
            case START_DATE_ASC -> new Order(Sort.Direction.ASC, "startDate");
            case START_DATE_DESC -> new Order(Sort.Direction.DESC, "startDate");
            case END_DATE_ASC -> new Order(Sort.Direction.ASC, "endDate");
            case END_DATE_DESC -> new Order(Sort.Direction.DESC, "endDate");

        };
    }

    private <T> List<T> nullIfEmpty(List<T> list) {
        return (list != null && !list.isEmpty()) ? list : null;
    }
    
    @Override
    protected void validate(SoftwareEntity entity, Long id) {
        if (entity == null) {
            throw new IllegalArgumentException("Software entity is null");
        }
        validateStringField(entity.getName(), "Name");
        validateStringField(entity.getDescription(), "Description");

        Date start = entity.getStartDate();
        Date end = entity.getEndDate();

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        if (end.before(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        if (entity.getTasks() == null) {
            throw new IllegalArgumentException("Task list must not be null");
        }
    }

}

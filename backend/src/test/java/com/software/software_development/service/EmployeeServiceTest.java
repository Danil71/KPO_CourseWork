package com.software.software_development.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.repository.EmployeeRepository;
import com.software.software_development.service.entity.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    @DisplayName("Create: Успешное сохранение")
    void create_ShouldSave_WhenValid() {
        DepartmentEntity dept = new DepartmentEntity("IT", 1, 100);
        EmployeeEntity entity = new EmployeeEntity("Ivan", dept);

        when(repository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> {
            EmployeeEntity e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        EmployeeEntity result = service.create(entity);

        assertNotNull(result.getId());
        assertEquals("Ivan", result.getName());
        verify(repository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Create: Ошибка если имя null")
    void create_ShouldThrow_WhenNameIsNull() {
        EmployeeEntity entity = new EmployeeEntity(null, new DepartmentEntity());
        
        assertThrows(IllegalArgumentException.class, () -> service.create(entity));
        
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка если имя невалидно (спецсимволы)")
    void create_ShouldThrow_WhenNameIsInvalid() {
        EmployeeEntity entity = new EmployeeEntity("Ivan#", new DepartmentEntity());

        assertThrows(IllegalArgumentException.class, () -> service.create(entity));
    }

    @Test
    @DisplayName("Get: Успешный поиск по ID")
    void get_ShouldReturnEntity_WhenFound() {
        EmployeeEntity entity = new EmployeeEntity("Ivan", null);
        entity.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(entity));

        EmployeeEntity result = service.get(10L);

        assertEquals(10L, result.getId());
        assertEquals("Ivan", result.getName());
    }

    @Test
    @DisplayName("Get: Ошибка NotFoundException если нет в базе")
    void get_ShouldThrow_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    @DisplayName("GetAll (List): Поиск всех или по фильтру")
    void getAllList_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(new EmployeeEntity("1", null), new EmployeeEntity("2", null)));
        
        List<EmployeeEntity> listAll = service.getAll(null);
        assertEquals(2, listAll.size());

        when(repository.findByNameContainingIgnoreCase("Alex")).thenReturn(List.of(new EmployeeEntity("Alex", null)));
        
        List<EmployeeEntity> listFiltered = service.getAll("Alex");
        assertEquals(1, listFiltered.size());
    }

    @Test
    @DisplayName("GetAll (Page): Пагинация")
    void getAllPage_ShouldReturnPage() {
        Page<EmployeeEntity> emptyPage = new PageImpl<>(Collections.emptyList());
        when(repository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<EmployeeEntity> result = service.getAll(null, 0, 5);

        assertNotNull(result);
        verify(repository).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("GetByIds: Получение списка по ID")
    void getByIds_ShouldReturnList() {
        List<Long> ids = List.of(1L, 2L);
        when(repository.findAllById(ids)).thenReturn(List.of(new EmployeeEntity(), new EmployeeEntity()));

        List<EmployeeEntity> result = service.getByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Update: Успешное обновление")
    void update_ShouldUpdateFields() {

        DepartmentEntity oldDept = new DepartmentEntity("Old", 1, 1);
        EmployeeEntity existing = new EmployeeEntity("OldName", oldDept);
        existing.setId(1L);

        DepartmentEntity newDept = new DepartmentEntity("New", 2, 2);
        EmployeeEntity updateInfo = new EmployeeEntity("NewName", newDept);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        EmployeeEntity result = service.update(1L, updateInfo);

        assertEquals("NewName", result.getName());
        assertEquals("New", result.getDepartment().getName());
    }

    @Test
    @DisplayName("Delete: Успешное удаление")
    void delete_ShouldCallRepoDelete() {
        EmployeeEntity existing = new EmployeeEntity();
        existing.setId(5L);

        when(repository.findById(5L)).thenReturn(Optional.of(existing));

        service.delete(5L);

        verify(repository).delete(existing);
    }
}
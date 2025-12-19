package com.software.software_development.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.repository.DepartmentRepository;
import com.software.software_development.service.entity.DepartmentService;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentService service;

    @Test
    @DisplayName("Create: Успешное создание (имя уникально)")
    void create_ShouldSave_WhenNameIsUnique() {
        DepartmentEntity entity = new DepartmentEntity("IT", 1, 100);

        when(repository.findByNameIgnoreCase("IT")).thenReturn(Optional.empty());

        when(repository.save(any(DepartmentEntity.class))).thenAnswer(i -> {
            DepartmentEntity d = i.getArgument(0);
            d.setId(1L);
            return d;
        });

        DepartmentEntity result = service.create(entity);

        assertNotNull(result.getId());
        assertEquals("IT", result.getName());
        verify(repository).save(any(DepartmentEntity.class));
    }

    @Test
    @DisplayName("Create: Ошибка, если имя уже занято")
    void create_ShouldThrow_WhenNameExists() {
        DepartmentEntity entity = new DepartmentEntity("HR", 1, 50);
        DepartmentEntity existing = new DepartmentEntity("HR", 2, 60);
        existing.setId(5L);

        when(repository.findByNameIgnoreCase("HR")).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> service.create(entity));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если entity null")
    void create_ShouldThrow_WhenNull() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
    }

    @Test
    @DisplayName("Get: Успешный поиск по ID")
    void get_ShouldReturnEntity_WhenFound() {
        DepartmentEntity entity = new DepartmentEntity("Sales", 1, 1);
        entity.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(entity));

        DepartmentEntity result = service.get(10L);
        assertEquals(10L, result.getId());
    }

    @Test
    @DisplayName("Get: Ошибка NotFoundException")
    void get_ShouldThrow_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    @DisplayName("GetAll: Возврат списка (с фильтром и без)")
    void getAll_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(new DepartmentEntity("A", 1, 1)));
        assertEquals(1, service.getAll(null).size());

        when(repository.findByNameContainingIgnoreCase("Tech")).thenReturn(List.of(new DepartmentEntity("Tech", 1, 1)));
        assertEquals(1, service.getAll("Tech").size());
    }

    @Test
    @DisplayName("Update: Успешное обновление полей")
    void update_ShouldUpdateFields() {
        DepartmentEntity existing = new DepartmentEntity("Old Name", 10, 50);
        existing.setId(1L);
        existing.setTasks(new HashSet<>());

        DepartmentEntity updateInfo = new DepartmentEntity("New Name", 20, 80);
        updateInfo.setTasks(new HashSet<>());

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findByNameIgnoreCase("New Name")).thenReturn(Optional.empty());
        when(repository.save(existing)).thenReturn(existing);

        DepartmentEntity result = service.update(1L, updateInfo);

        assertEquals("New Name", result.getName());
        assertEquals(20, result.getSpecialty());
        assertEquals(80, result.getEfficiency());
    }

    @Test
    @DisplayName("Update: Синхронизация задач (удаление старых, добавление новых)")
    void update_ShouldSyncTasks() {
        TaskEntity t1 = new TaskEntity(); t1.setId(10L);
        DepartmentEntity existing = new DepartmentEntity("Dept", 1, 1);
        existing.setId(1L);
        existing.setTasks(new HashSet<>(Set.of(t1)));

        TaskEntity t2 = new TaskEntity(); t2.setId(20L);
        DepartmentEntity updateInfo = new DepartmentEntity("Dept", 1, 1);
        updateInfo.setTasks(new HashSet<>(Set.of(t2)));

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findByNameIgnoreCase("Dept")).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        DepartmentEntity result = service.update(1L, updateInfo);

        assertEquals(1, result.getTasks().size());
        assertTrue(result.getTasks().contains(t2), "Новая задача должна быть");
        assertFalse(result.getTasks().contains(t1), "Старая задача должна быть удалена");
    }

    @Test
    @DisplayName("Update: Ошибка, если новое имя занято другим отделом")
    void update_ShouldThrow_WhenDuplicateName() {
        DepartmentEntity existing = new DepartmentEntity("My Dept", 1, 1);
        existing.setId(1L);

        DepartmentEntity updateInfo = new DepartmentEntity("Occupied Name", 1, 1);

        DepartmentEntity occupied = new DepartmentEntity("Occupied Name", 2, 2);
        occupied.setId(2L);

        when(repository.findByNameIgnoreCase("Occupied Name")).thenReturn(Optional.of(occupied));

        assertThrows(IllegalArgumentException.class, () -> service.update(1L, updateInfo));
    }
    
    @Test
    @DisplayName("Delete: Успешное удаление")
    void delete_ShouldCallRepoDelete() {
        DepartmentEntity existing = new DepartmentEntity();
        existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repository).delete(existing);
    }
}
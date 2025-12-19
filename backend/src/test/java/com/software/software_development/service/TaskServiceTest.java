package com.software.software_development.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.SoftwareEntity;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.repository.TaskRepository;
import com.software.software_development.service.entity.TaskService;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private TaskEntity createValidTask() {
        return new TaskEntity(
            "Fix critical bug", 
            5,
            new Date(), 
            new Date(), 
            8.0f, 
            new SoftwareEntity()
        );
    }

    @Test
    @DisplayName("Create: Успешное создание задачи (валидная сложность)")
    void create_ShouldSave_WhenValid() {
        TaskEntity inputTask = createValidTask();

        when(repository.save(any(TaskEntity.class))).thenAnswer(i -> {
            TaskEntity t = i.getArgument(0);
            t.setId(1L);
            return t;
        });

        service.create(inputTask);

        ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(repository).save(captor.capture());
        
        TaskEntity saved = captor.getValue();
        assertEquals("Fix critical bug", saved.getDescription());
        assertEquals(5, saved.getDifficulty());
    }

    @Test
    @DisplayName("Create: Ошибка, если сложность слишком низкая (< 1)")
    void create_ShouldThrow_WhenDifficultyTooLow() {
        TaskEntity inputTask = createValidTask();
        inputTask.setDifficulty(0);

        assertThrows(IllegalArgumentException.class, () -> service.create(inputTask));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если сложность слишком высокая (> 10)")
    void create_ShouldThrow_WhenDifficultyTooHigh() {
        TaskEntity inputTask = createValidTask();
        inputTask.setDifficulty(11);

        assertThrows(IllegalArgumentException.class, () -> service.create(inputTask));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если описание пустое")
    void create_ShouldThrow_WhenDescriptionEmpty() {
        TaskEntity inputTask = createValidTask();
        inputTask.setDescription("");

        assertThrows(IllegalArgumentException.class, () -> service.create(inputTask));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Get: Успешное получение по ID")
    void get_ExistingId_ShouldReturnTask() {
        TaskEntity task = createValidTask();
        task.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        TaskEntity result = service.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Get: Ошибка NotFoundException")
    void get_NonExistingId_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    @DisplayName("GetAll (List): Поиск всех (описание null)")
    void getAllList_NullDesc_ShouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(createValidTask()));
        
        List<TaskEntity> result = service.getAll(null);
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("GetAll (List): Поиск по описанию")
    void getAllList_WithDesc_ShouldFilter() {
        String desc = "Bug";
        when(repository.findByDescriptionContainingIgnoreCase(desc)).thenReturn(List.of(createValidTask()));
        
        List<TaskEntity> result = service.getAll(desc);
        assertEquals(1, result.size());
        verify(repository).findByDescriptionContainingIgnoreCase(desc);
    }

    @Test
    @DisplayName("GetAll (Page): Пагинация с фильтром")
    void getAllPage_WithDesc_ShouldReturnPage() {
        String desc = "Fix";
        Pageable pageable = PageRequest.of(0, 5);
        Page<TaskEntity> page = new PageImpl<>(List.of(createValidTask()));

        when(repository.findByNameContainingIgnoreCase(desc, pageable)).thenReturn(page);

        Page<TaskEntity> result = service.getAll(desc, 0, 5);
        assertEquals(1, result.getTotalElements());
    }
    
    @Test
    @DisplayName("GetByIds: Получение по списку ID")
    void getByIds_ShouldReturnList() {
        List<Long> ids = List.of(1L, 2L);
        when(repository.findAllById(ids)).thenReturn(List.of(createValidTask(), createValidTask()));
        
        List<TaskEntity> result = service.getByIds(ids);
        assertEquals(2, result.size());
    }
    
    @Test
    @DisplayName("GetByIds: Пустой список если ID null")
    void getByIds_Null_ShouldReturnEmpty() {
        assertTrue(service.getByIds(null).isEmpty());
        verify(repository, never()).findAllById(any());
    }

    @Test
    @DisplayName("Update: Успешное обновление")
    void update_ShouldUpdateFields() {
        Long id = 1L;
        TaskEntity existing = createValidTask();
        existing.setId(id);
        existing.setDifficulty(5);

        TaskEntity updateInfo = createValidTask();
        updateInfo.setDescription("New Desc");
        updateInfo.setDifficulty(10);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        
        service.update(id, updateInfo);
        ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(repository).save(captor.capture());
        
        assertEquals("New Desc", captor.getValue().getDescription());
        assertEquals(10, captor.getValue().getDifficulty());
    }

    @Test
    @DisplayName("Update: Ошибка при невалидной сложности")
    void update_InvalidDifficulty_ShouldThrowException() {
        Long id = 1L;
        TaskEntity updateInfo = createValidTask();
        updateInfo.setDifficulty(100);

        assertThrows(IllegalArgumentException.class, () -> service.update(id, updateInfo));
        
        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Update: Ошибка NotFound")
    void update_NotFound_ShouldThrowException() {
        Long id = 99L;
        TaskEntity updateInfo = createValidTask();
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(id, updateInfo));
    }

    @Test
    @DisplayName("Delete: Успешное удаление")
    void delete_ShouldCallRepo() {
        TaskEntity existing = createValidTask();
        existing.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);
        verify(repository).delete(existing);
    }
}
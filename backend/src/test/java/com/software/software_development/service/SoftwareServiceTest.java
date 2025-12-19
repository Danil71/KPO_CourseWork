package com.software.software_development.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import org.springframework.data.domain.Sort;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.SoftwareEntity;
import com.software.software_development.model.enums.SoftwareSortType;
import com.software.software_development.repository.SoftwareRepository;
import com.software.software_development.service.entity.SoftwareService;

@ExtendWith(MockitoExtension.class)
class SoftwareServiceTest {

    @Mock
    private SoftwareRepository repository;

    @InjectMocks
    private SoftwareService service;

    private SoftwareEntity createValidSoftware() {
        long now = System.currentTimeMillis();
        SoftwareEntity soft = new SoftwareEntity(
            "IntelliJ IDEA",
            "IDE for Java",
            new Date(now), 
            new Date(now + 86400000)
        );
        soft.setTasks(new HashSet<>());
        return soft;
    }

    @Test
    @DisplayName("Create: Успешное создание")
    void create_ShouldSave_WhenValid() {
        SoftwareEntity input = createValidSoftware();

        when(repository.save(any(SoftwareEntity.class))).thenAnswer(i -> {
            SoftwareEntity s = i.getArgument(0);
            s.setId(1L);
            return s;
        });

        SoftwareEntity result = service.create(input);

        assertNotNull(result.getId());
        verify(repository).save(input);
    }

    @Test
    @DisplayName("Create: Ошибка, если Дата Конца раньше Даты Начала")
    void create_ShouldThrow_WhenDatesInvalid() {
        SoftwareEntity input = createValidSoftware();
        
        long now = System.currentTimeMillis();
        input.setStartDate(new Date(now));
        input.setEndDate(new Date(now - 1000));

        assertThrows(IllegalArgumentException.class, () -> service.create(input));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если Tasks == null")
    void create_ShouldThrow_WhenTasksNull() {
        SoftwareEntity input = createValidSoftware();
        input.setTasks(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(input));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если имя пустое")
    void create_ShouldThrow_WhenNameEmpty() {
        SoftwareEntity input = createValidSoftware();
        input.setName(""); 

        assertThrows(IllegalArgumentException.class, () -> service.create(input));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Get: Успешное получение по ID")
    void get_ExistingId_ShouldReturnEntity() {
        SoftwareEntity soft = createValidSoftware();
        soft.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(soft));

        assertEquals(1L, service.get(1L).getId());
    }

    @Test
    @DisplayName("Get: Ошибка NotFound")
    void get_NonExistingId_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    @DisplayName("GetAll (List): Возврат списка")
    void getAll_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(createValidSoftware()));
        assertEquals(1, service.getAll().size());
    }

    @Test
    @DisplayName("GetAll (Page): Пагинация с фильтром по имени")
    void getAllPage_ShouldFilter() {
        Pageable p = PageRequest.of(0, 5);
        when(repository.findByNameContainingIgnoreCase("Idea", p))
                .thenReturn(new PageImpl<>(List.of(createValidSoftware())));
        
        assertEquals(1, service.getAll("Idea", 0, 5).getTotalElements());
    }

    @Test
    @DisplayName("GetAllByFilters: Проверка преобразования параметров")
    void getAllByFilters_ShouldPassCorrectParamsToRepo() {
        Date start = new Date();
        Date end = new Date();
        List<Long> taskIds = Collections.emptyList();
        String search = "TeSt";
        int page = 0;
        int size = 10;
        SoftwareSortType sortType = SoftwareSortType.NAME_ASC;

        when(repository.findByFilters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        service.getAllByFilters(start, start, end, end, taskIds, search, sortType, page, size);

        verify(repository).findByFilters(
            eq(start), 
            eq(start), 
            eq(end), 
            eq(end), 
            isNull(),
            eq("test"),
            any(Pageable.class)
        );
    }
    
    @Test
    @DisplayName("GetAllByFilters: Сортировка по умолчанию (DESC), если sortType null")
    void getAllByFilters_NullSort_ShouldUseDefault() {
        when(repository.findByFilters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        service.getAllByFilters(null, null, null, null, null, null, null, 0, 10);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findByFilters(
            any(), any(), any(), any(), any(), any(), 
            captor.capture()
        );

        Sort sort = captor.getValue().getSort();
        assertEquals(Sort.Direction.DESC, sort.getOrderFor("name").getDirection());
    }

    @Test
    @DisplayName("Update: Успешное обновление")
    void update_ShouldUpdateFields() {
        Long id = 1L;
        SoftwareEntity existing = createValidSoftware();
        existing.setId(id);
        existing.setName("Old Name");

        SoftwareEntity updateInfo = createValidSoftware();
        updateInfo.setName("New Name");
        updateInfo.setDescription("New Desc");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        service.update(id, updateInfo);

        verify(repository).save(existing);
        assertEquals("New Name", existing.getName());
        assertEquals("New Desc", existing.getDescription());
    }

    @Test
    @DisplayName("Update: Ошибка валидации дат")
    void update_InvalidDates_ShouldThrowException() {
        Long id = 1L;
        SoftwareEntity updateInfo = createValidSoftware();
        
        long now = System.currentTimeMillis();
        updateInfo.setStartDate(new Date(now));
        updateInfo.setEndDate(new Date(now - 1000));

        assertThrows(IllegalArgumentException.class, () -> service.update(id, updateInfo));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Update: Ошибка NotFound")
    void update_NotFound_ShouldThrowException() {
        Long id = 99L;
        SoftwareEntity updateInfo = createValidSoftware();
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(id, updateInfo));
    }

    @Test
    @DisplayName("Delete: Успешное удаление")
    void delete_ShouldCallRepo() {
        SoftwareEntity existing = createValidSoftware();
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);
        verify(repository).delete(existing);
    }
}
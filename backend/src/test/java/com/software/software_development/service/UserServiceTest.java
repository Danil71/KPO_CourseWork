package com.software.software_development.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.software.software_development.core.error.NotFoundException;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.enums.UserRole;
import com.software.software_development.repository.UserRepository;
import com.software.software_development.service.entity.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private UserEntity createValidUser() {
        return new UserEntity(
            "test@example.com",
            "StrongPass1!", 
            "+79001234567",
            UserRole.MANAGER,
            new EmployeeEntity() 
        );
    }

    @Test
    @DisplayName("Create: Успешное создание (Хеширование пароля + Нормализация телефона)")
    void create_ShouldSuccess_WhenValid() {
        UserEntity inputUser = new UserEntity(
                "new@example.com",
                "StrongPass1!",
                "8 (999) 111-22-33", 
                UserRole.MANAGER,
                new EmployeeEntity()
        );

        when(repository.findByEmailIgnoreCase(inputUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("StrongPass1!")).thenReturn("HashedSecret$$$");
        when(repository.save(any(UserEntity.class))).thenAnswer(i -> {
            UserEntity u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        service.create(inputUser);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).save(captor.capture());
        UserEntity savedUser = captor.getValue();

        assertEquals("HashedSecret$$$", savedUser.getPassword());
        assertEquals("+79991112233", savedUser.getPhoneNumber());
        assertEquals("new@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Create: Ошибка, если Email занят")
    void create_ShouldThrow_WhenEmailDuplicate() {
        UserEntity inputUser = createValidUser();
        UserEntity existingUser = createValidUser();
        existingUser.setId(5L);

        when(repository.findByEmailIgnoreCase(inputUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> service.create(inputUser));
        
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если пароль невалидный (слабый)")
    void create_ShouldThrow_WhenPasswordInvalid() {
        UserEntity inputUser = createValidUser();
        inputUser.setPassword("weak");

        assertThrows(IllegalArgumentException.class, () -> service.create(inputUser));

        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Create: Ошибка, если телефон невалидный")
    void create_ShouldThrow_WhenPhoneInvalid() {
        UserEntity inputUser = createValidUser();
        inputUser.setPhoneNumber("not-a-number");

        assertThrows(IllegalArgumentException.class, () -> service.create(inputUser));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Update: Успешная смена пароля")
    void update_ShouldHashPassword_WhenChanged() {
        Long id = 1L;
        UserEntity existing = createValidUser();
        existing.setId(id);
        existing.setPassword("OldHash");

        UserEntity updateInfo = createValidUser();
        updateInfo.setPassword("NewStrongPass1!"); 

        when(repository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("NewStrongPass1!")).thenReturn("NewHash$$$");
        when(repository.save(existing)).thenReturn(existing);

        service.update(id, updateInfo);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).save(captor.capture());
        
        assertEquals("NewHash$$$", captor.getValue().getPassword());
    }

    @Test
    @DisplayName("Update: Успешное обновление без смены пароля")
    void update_ShouldKeepOldPassword_WhenNullOrBlank() {
        Long id = 1L;
        UserEntity existing = createValidUser();
        existing.setId(id);
        existing.setPassword("OldHash");

        UserEntity updateInfo = createValidUser();
        updateInfo.setPassword(null);

        when(repository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        service.update(id, updateInfo);

        verify(passwordEncoder, never()).encode(anyString());
        
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).save(captor.capture());
        assertEquals("OldHash", captor.getValue().getPassword());
    }

    @Test
    @DisplayName("Update: Ошибка, если новый пароль невалидный")
    void update_ShouldThrow_WhenNewPasswordInvalid() {
        Long id = 1L;
        UserEntity updateInfo = createValidUser();
        updateInfo.setPassword("weak");

        assertThrows(IllegalArgumentException.class, () -> service.update(id, updateInfo));

        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Update: Ошибка, если Email занят другим пользователем")
    void update_ShouldThrow_WhenEmailDuplicate() {
        Long id = 1L;
        UserEntity updateInfo = createValidUser();
        updateInfo.setEmail("busy@example.com");

        UserEntity otherUser = createValidUser();
        otherUser.setId(2L);

        when(repository.findByEmailIgnoreCase("busy@example.com")).thenReturn(Optional.of(otherUser));

        assertThrows(IllegalArgumentException.class, () -> service.update(id, updateInfo));

        verify(repository, never()).findById(id);
        verify(repository, never()).save(any());
    }
    
    @Test
    @DisplayName("Update: Ошибка NotFound, если ID не найден")
    void update_ShouldThrowNotFound_WhenIdMissing() {
        Long id = 99L;
        UserEntity updateInfo = createValidUser();

        when(repository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(id, updateInfo));
    }

    @Test
    @DisplayName("Get: Успешный поиск")
    void get_ShouldReturnUser() {
        UserEntity user = createValidUser();
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        
        assertNotNull(service.get(1L));
    }

    @Test
    @DisplayName("Get: Ошибка NotFound")
    void get_ShouldThrow_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    @DisplayName("GetByEmail: Успешный поиск")
    void getByEmail_ShouldReturnUser() {
        UserEntity user = createValidUser();
        when(repository.findByEmailIgnoreCase("a@b.c")).thenReturn(Optional.of(user));
        
        assertEquals(user, service.getByEmail("a@b.c"));
    }

    @Test
    @DisplayName("GetByEmail: Ошибка IllegalArgumentException (по вашей логике)")
    void getByEmail_ShouldThrow_WhenNotFound() {
        when(repository.findByEmailIgnoreCase("unknown")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getByEmail("unknown"));
    }

    @Test
    @DisplayName("GetAll: Возврат списка")
    void getAll_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(createValidUser()));
        assertEquals(1, service.getAll().size());
    }

    @Test
    @DisplayName("Delete: Успешное удаление")
    void delete_ShouldCallRepo() {
        UserEntity user = createValidUser();
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.delete(1L);

        verify(repository).delete(user);
    }
}
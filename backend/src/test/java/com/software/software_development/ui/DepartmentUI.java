package com.software.software_development.ui;


import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.software.software_development.ui.pages.DepartmentPage;
import com.software.software_development.ui.pages.LoginPage;
import com.software.software_development.ui.pages.NavigationPage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentUI extends BaseTest {

    private static final String NAME = generateUniqueName("Dept-");
    private static final String EDITED_NAME = generateUniqueName("Upd-");

    @BeforeEach
    void loginAndNavigate() {
        driver.get(BASE_URL);
        
        new LoginPage(driver).loginFull("daniilputincev91@gmail.com", "DsD#Ys9dS_cQ");
        
        new NavigationPage(driver).goToDepartments();
    }

    @Test
    @Order(1)
    @DisplayName("Department: Создание")
    void testCreate() {
        DepartmentPage page = new DepartmentPage(driver);
        page.create(NAME, 5, 90);
        
        Assertions.assertTrue(page.isVisible(NAME), "Созданный отдел должен появиться в таблице");
    }

    @Test
    @Order(2)
    @DisplayName("Department: Редактирование")
    void testEdit() {
        DepartmentPage page = new DepartmentPage(driver);
        page.edit(NAME, EDITED_NAME);
        
        Assertions.assertTrue(page.isVisible(EDITED_NAME), "Новое имя отдела должно быть видно");
        Assertions.assertFalse(page.isVisible(NAME), "Старое имя отдела должно исчезнуть");
    }

    @Test
    @Order(3)
    @DisplayName("Department: Удаление")
    void testDelete() {
        DepartmentPage page = new DepartmentPage(driver);
        page.delete(EDITED_NAME);
        
        Assertions.assertFalse(page.isVisible(EDITED_NAME), "Отдел должен быть удален из таблицы");
    }

    private static String generateUniqueName(String prefix) {
        String timestampAlpha = String.valueOf(System.currentTimeMillis())
                .chars()
                .mapToObj(c -> String.valueOf((char) (c - '0' + 'a')))
                .collect(Collectors.joining());
        
        String suffix = timestampAlpha.substring(timestampAlpha.length() - 10);
        return prefix + suffix;
    }
}

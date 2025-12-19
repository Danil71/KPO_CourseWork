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
import com.software.software_development.ui.pages.EmployeePage;
import com.software.software_development.ui.pages.LoginPage;
import com.software.software_development.ui.pages.NavigationPage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeUI extends BaseTest {

    private static final String DEPT_NAME = "EmpSupport";
    private static final String NAME = generateUniqueName("Soft-");
    private static final String EDITED_NAME = generateUniqueName("Upd-");

    @BeforeEach
    void loginAndNavigate() {
        driver.get(BASE_URL);
        new LoginPage(driver).loginFull("daniilputincev91@gmail.com", "DsD#Ys9dS_cQ");
    }

    @Test
    @Order(1)
    @DisplayName("Prep: Создание департамента для сотрудника")
    void prepareDepartment() {
        new NavigationPage(driver).goToDepartments();
        new DepartmentPage(driver).create(DEPT_NAME, 1, 1);
    }

    @Test
    @Order(2)
    @DisplayName("Employee: Создание (с привязкой к отделу)")
    void createEmployee() {
        new NavigationPage(driver).goToEmployees();
        
        EmployeePage page = new EmployeePage(driver);
        page.create(NAME, DEPT_NAME); 
        
        Assertions.assertTrue(page.isVisible(NAME), "Сотрудник должен появиться в таблице");
    }

    @Test
    @Order(3)
    @DisplayName("Employee: Редактирование")
    void editEmployee() {
        new NavigationPage(driver).goToEmployees();
        
        EmployeePage page = new EmployeePage(driver);
        page.editName(NAME, EDITED_NAME);
        
        Assertions.assertTrue(page.isVisible(EDITED_NAME), "Сотрудник должен быть переименован");
    }

    @Test
    @Order(4)
    @DisplayName("Cleanup: Удаление сотрудника и отдела")
    void cleanup() {
        NavigationPage nav = new NavigationPage(driver);

        nav.goToEmployees();
        EmployeePage empPage = new EmployeePage(driver);
        empPage.delete(EDITED_NAME);
        Assertions.assertFalse(empPage.isVisible(EDITED_NAME));

        nav.goToDepartments();
        DepartmentPage depPage = new DepartmentPage(driver);
        depPage.delete(DEPT_NAME);
        Assertions.assertFalse(depPage.isVisible(DEPT_NAME));
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

package com.software.software_development.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.software.software_development.ui.pages.LoginPage;
import com.software.software_development.ui.pages.NavigationPage;
import com.software.software_development.ui.pages.SoftwarePage;
import com.software.software_development.ui.pages.TaskPage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskUI extends BaseTest {

    private static final String SOFT_NAME = generateUniqueName("TaskHost-");
    private static final String TASK_DESC = generateUniqueName("Task-");
    private static final String TASK_EDITED = generateUniqueName("Upd-");

    private static Date START_DATE;
    private static Date END_DATE;

    @BeforeEach
    void start() {

        START_DATE = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(START_DATE);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        END_DATE = cal.getTime();

        driver.get(BASE_URL);
        new LoginPage(driver).loginFull("daniilputincev91@gmail.com", "DsD#Ys9dS_cQ");
    }

    @Test
    @Order(1)
    @DisplayName("Prep: Создать ПО для задачи")
    void createSoftware() {
        new NavigationPage(driver).goToSoftware();

        new SoftwarePage(driver).create(SOFT_NAME, "Host for tasks", START_DATE, END_DATE);
    }

    @Test
    @Order(2)
    @DisplayName("Task: Создание")
    void createTask() {
        new NavigationPage(driver).goToTasks();
        
        TaskPage page = new TaskPage(driver);

        page.create(TASK_DESC, 5, 8, START_DATE, END_DATE, SOFT_NAME);
        
        Assertions.assertTrue(page.isVisible(TASK_DESC));
    }

    @Test
    @Order(3)
    @DisplayName("Task: Редактирование")
    void editTask() {
        new NavigationPage(driver).goToTasks();
        
        TaskPage page = new TaskPage(driver);
        page.editDescription(TASK_DESC, TASK_EDITED);
        
        Assertions.assertTrue(page.isVisible(TASK_EDITED));
    }

    @Test
    @Order(4)
    @DisplayName("Cleanup: Удалить Задачу и ПО")
    void cleanup() {
        NavigationPage nav = new NavigationPage(driver);

        nav.goToTasks();
        TaskPage taskPage = new TaskPage(driver);
        if (taskPage.isVisible(TASK_EDITED)) {
            taskPage.delete(TASK_EDITED);
            Assertions.assertFalse(taskPage.isVisible(TASK_EDITED));
        }

        nav.goToSoftware();
        SoftwarePage softPage = new SoftwarePage(driver);
        if (softPage.isVisible(SOFT_NAME)) {
            softPage.delete(SOFT_NAME);
            Assertions.assertFalse(softPage.isVisible(SOFT_NAME));
        }
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
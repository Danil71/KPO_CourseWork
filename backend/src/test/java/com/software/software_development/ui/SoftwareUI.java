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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SoftwareUI extends BaseTest {

    private static final String NAME = generateUniqueName("Soft-");
    private static final String EDITED_NAME = generateUniqueName("Upd-");
    
    private static Date START_DATE;
    private static Date END_DATE;

    @BeforeEach
    void start() {
        START_DATE = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(START_DATE);
        cal.add(Calendar.MONTH, 1);
        END_DATE = cal.getTime();

        driver.get(BASE_URL);
        new LoginPage(driver).loginFull("daniilputincev91@gmail.com", "DsD#Ys9dS_cQ");
        new NavigationPage(driver).goToSoftware();
    }

    @Test
    @Order(1)
    @DisplayName("Software: Создание")
    void testCreate() {
        SoftwarePage page = new SoftwarePage(driver);
        page.create(NAME, "AutoDescription", START_DATE, END_DATE);
        
        Assertions.assertTrue(page.isVisible(NAME), "ПО должно появиться в списке");
    }

    @Test
    @Order(2)
    @DisplayName("Software: Поиск и Фильтр")
    void testSearch() {
        SoftwarePage page = new SoftwarePage(driver);
        page.search(NAME);
        
        Assertions.assertTrue(page.isVisible(NAME));
        Assertions.assertEquals(1, page.getRowsCount(), "Должна быть найдена 1 запись");
    }

    @Test
    @Order(3)
    @DisplayName("Software: Редактирование")
    void testEdit() {
        SoftwarePage page = new SoftwarePage(driver);
        page.search(""); 
        
        page.edit(NAME, EDITED_NAME);
        
        Assertions.assertTrue(page.isVisible(EDITED_NAME), "Имя должно обновиться");
        Assertions.assertFalse(page.isVisible(NAME));
    }

    @Test
    @Order(4)
    @DisplayName("Software: Скачивание отчета")
    void testDownload() {
        SoftwarePage page = new SoftwarePage(driver);
        Assertions.assertDoesNotThrow(page::downloadReport);
    }

    @Test
    @Order(5)
    @DisplayName("Software: Удаление")
    void testDelete() {
        SoftwarePage page = new SoftwarePage(driver);
        if (page.isVisible(EDITED_NAME)) {
            page.delete(EDITED_NAME);
            Assertions.assertFalse(page.isVisible(EDITED_NAME), "ПО должно удалиться");
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
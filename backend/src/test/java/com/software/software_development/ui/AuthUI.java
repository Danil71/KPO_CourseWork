package com.software.software_development.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.software.software_development.ui.pages.LoginPage;
import com.software.software_development.ui.pages.NavigationPage;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthUI extends BaseTest {

     private static final String EMAIL = "daniilputincev91@gmail.com";
    private static final String PASSWORD = "DsD#Ys9dS_cQ";

    @Test
    @Order(1)
    @DisplayName("Login: Вход в систему (появление навигации)")
    void testLoginSuccess() {
        driver.get(BASE_URL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginFull(EMAIL, PASSWORD);

        NavigationPage nav = new NavigationPage(driver);
        Assertions.assertTrue(nav.isNavigationVisible(), 
            "После входа должна появиться навигационная панель");
    }

    @Test
    @Order(2)
    @DisplayName("Logout: Выход из системы (появление формы входа)")
    void testLogout() {
        NavigationPage nav = new NavigationPage(driver);
        if (!nav.isNavigationVisible()) {
            driver.get(BASE_URL);
            new LoginPage(driver).loginFull(EMAIL, PASSWORD);
        }

        nav.logout();

        LoginPage loginPage = new LoginPage(driver);
        Assertions.assertTrue(loginPage.isLoginVisible(), 
            "После выхода должна отобразиться форма входа");
    }
}

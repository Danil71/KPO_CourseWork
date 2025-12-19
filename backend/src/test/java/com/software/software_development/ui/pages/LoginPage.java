package com.software.software_development.ui.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailField = By.cssSelector("[data-testid='login-email']");
    private final By passwordField = By.cssSelector("[data-testid='login-password']");
    private final By loginBtn = By.cssSelector("[data-testid='login-submit']");

    private final By otpInput = By.cssSelector("[data-testid='otp-input']");
    private final By otpSubmitBtn = By.cssSelector("[data-testid='otp-submit-btn']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void loginFull(String email, String password) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));

        driver.findElement(emailField).clear();
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginBtn).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(otpInput));

        driver.findElement(otpInput).sendKeys("111111");
        driver.findElement(otpSubmitBtn).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(otpInput));
    }

    public boolean isLoginVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtn));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

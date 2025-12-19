package com.software.software_development.ui.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NavigationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By managementDropdown = By.cssSelector("[data-testid='nav-management-dropdown']");

    private final By userDropdown = By.cssSelector("[data-testid='nav-user-dropdown']");
    private final By logoutBtn = By.cssSelector("[data-testid='logout-btn']");

    private final By softwareLink = By.cssSelector("[data-testid='nav-link-software']");
    private final By taskLink = By.cssSelector("[data-testid='nav-link-task']");
    private final By departmentLink = By.cssSelector("[data-testid='nav-link-department']");
    private final By userLink = By.cssSelector("[data-testid='nav-link-user']");
    private final By employeeLink = By.cssSelector("[data-testid='nav-link-employee']");

    public NavigationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void goToSoftware() {
        openManagementMenu();
        clickLink(softwareLink);
    }

    public void goToTasks() {
        openManagementMenu();
        clickLink(taskLink);
    }

    public void goToDepartments() {
        openManagementMenu();
        clickLink(departmentLink);
    }

    public void goToEmployees() {
        openManagementMenu();
        clickLink(employeeLink);
    }

    public void goToUsers() {
        openManagementMenu();
        clickLink(userLink);
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown));
        driver.findElement(userDropdown).click();

        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn));
        driver.findElement(logoutBtn).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(managementDropdown));
    }

    public boolean isNavigationVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(managementDropdown));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void openManagementMenu() {

        wait.until(ExpectedConditions.elementToBeClickable(managementDropdown));
        driver.findElement(managementDropdown).click();
    }

    private void clickLink(By locator) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).click();
    }
}
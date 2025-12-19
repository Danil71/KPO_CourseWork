package com.software.software_development.ui.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DepartmentPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By createBtn = By.cssSelector("[data-testid='dept-create-btn']");
    
    private final By nameInput = By.cssSelector("[data-testid='dept-name']");
    private final By specialtyInput = By.cssSelector("[data-testid='dept-specialty']");
    private final By efficiencyInput = By.cssSelector("[data-testid='dept-efficiency']");
    private final By confirmDeleteBtn = By.cssSelector("[data-testid='confirm-delete-btn']");
    
    private final By saveBtn = By.cssSelector("[data-testid='dept-save-btn']");
    

    public DepartmentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void create(String name, int specialty, int efficiency) {
        wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();
        fillForm(name, specialty, efficiency);
    }

    public void edit(String oldName, String newName) {
        WebElement row = getRow(oldName);
        row.findElement(By.cssSelector("[data-testid='edit-btn']")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        WebElement nameField = driver.findElement(nameInput);
        nameField.clear();
        nameField.sendKeys(newName);
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(nameInput)); // Ждем закрытия
    }

    public void delete(String name) {
        By rowLocator = By.cssSelector("[data-testid='row-" + name + "']");
        
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
        row.findElement(By.cssSelector("[data-testid='delete-btn']")).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn)).click();
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(rowLocator));
    }

    public boolean isVisible(String name) {
        try {
            By rowLocator = By.cssSelector("[data-testid='row-" + name + "']");
            return !driver.findElements(rowLocator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private void fillForm(String name, int specialty, int efficiency) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        
        driver.findElement(nameInput).sendKeys(name);
        driver.findElement(specialtyInput).sendKeys(String.valueOf(specialty));
        driver.findElement(efficiencyInput).sendKeys(String.valueOf(efficiency));
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(nameInput));
    }

    private WebElement getRow(String name) {
        By locator = By.cssSelector("[data-testid='row-" + name + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator);
    }
}
package com.software.software_development.ui.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeePage {
    private final WebDriver driver;
    private final WebDriverWait wait;


    private final By createBtn = By.cssSelector("[data-testid='emp-create-btn']");
    private final By nameInput = By.cssSelector("[data-testid='emp-name']");
    private final By deptSelect = By.cssSelector("[data-testid='emp-dept-select']");
    private final By saveBtn = By.cssSelector("[data-testid='emp-save-btn']");
    private final By confirmDeleteBtn = By.cssSelector(".modal-content button.btn-primary");

    public EmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void create(String name, String departmentName) {
        wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));

        driver.findElement(nameInput).sendKeys(name);

        Select select = new Select(driver.findElement(deptSelect));
        select.selectByVisibleText(departmentName);

        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(nameInput));
    }

    public void editName(String oldName, String newName) {
        WebElement row = getRow(oldName);
        row.findElement(By.cssSelector("[data-testid='edit-btn']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        WebElement input = driver.findElement(nameInput);
        input.clear();
        input.sendKeys(newName);
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(nameInput));
    }

    public void delete(String name) {
        WebElement row = getRow(name);
        row.findElement(By.cssSelector("[data-testid='delete-btn']")).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn)).click();
        wait.until(ExpectedConditions.invisibilityOf(row));
    }

    public boolean isVisible(String name) {
        try {
            return !driver.findElements(By.cssSelector("[data-testid='row-" + name + "']")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement getRow(String name) {
        By locator = By.cssSelector("[data-testid='row-" + name + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator);
    }
}

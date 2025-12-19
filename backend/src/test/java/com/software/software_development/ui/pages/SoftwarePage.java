package com.software.software_development.ui.pages;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SoftwarePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By createBtn = By.cssSelector("[data-testid='soft-create-btn']");
    private final By searchInput = By.cssSelector("[data-testid='soft-search-input']");
    private final By searchBtn = By.cssSelector("[data-testid='soft-search-btn']");
    private final By downloadBtnWrapper = By.cssSelector("[data-testid='soft-download-wrapper']");

    private final By nameInput = By.cssSelector("[data-testid='soft-name']");
    private final By descInput = By.cssSelector("[data-testid='soft-desc']");
    private final By startInput = By.cssSelector("[data-testid='soft-form-start']");
    private final By endInput = By.cssSelector("[data-testid='soft-form-end']");
    private final By saveBtn = By.cssSelector("[data-testid='soft-save-btn']");

    private final By confirmDeleteBtn = By.cssSelector(".modal-content button.btn-primary");

    public SoftwarePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void create(String name, String desc, Date start, Date end) {
        wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        driver.findElement(nameInput).sendKeys(name);
        driver.findElement(descInput).sendKeys(desc);
      
        fillDate(startInput, start);
        fillDate(endInput, end);
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(nameInput));
    }

    public void edit(String oldName, String newName) {
        WebElement row = getRow(oldName);
        row.findElement(By.cssSelector("[data-testid='edit-btn']")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        WebElement nameField = driver.findElement(nameInput);
        nameField.clear();
        nameField.sendKeys(newName);
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(nameInput));
    }

    public void delete(String name) {
        WebElement row = getRow(name);
        row.findElement(By.cssSelector("[data-testid='delete-btn']")).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn)).click();
        wait.until(ExpectedConditions.invisibilityOf(row));
    }

    public void search(String text) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(text);
        driver.findElement(searchBtn).click();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    public void downloadReport() {
        wait.until(ExpectedConditions.elementToBeClickable(downloadBtnWrapper)).click();
    }

    public boolean isVisible(String name) {
        try {
            return !driver.findElements(By.cssSelector("[data-testid='row-" + name + "']")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getRowsCount() {
        return driver.findElements(By.xpath("//tr[contains(@data-testid, 'row-')]")).size();
    }

    private WebElement getRow(String name) {
        By loc = By.cssSelector("[data-testid='row-" + name + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
        return driver.findElement(loc);
    }

    private void fillDate(By locator, Date date) {
        if (date == null) return;
        WebElement element = driver.findElement(locator);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String dateString = sdf.format(date);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = 
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));";
        
        js.executeScript(script, element, dateString);
    }
}
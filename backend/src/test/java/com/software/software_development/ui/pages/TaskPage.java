package com.software.software_development.ui.pages;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaskPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By createBtn = By.cssSelector("[data-testid='task-create-btn']");
    private final By descInput = By.cssSelector("[data-testid='task-desc']");
    private final By diffInput = By.cssSelector("[data-testid='task-diff']");
    private final By hoursInput = By.cssSelector("[data-testid='task-hours']");
    private final By startInput = By.cssSelector("[data-testid='task-start']");
    private final By endInput = By.cssSelector("[data-testid='task-end']");
    private final By softSelect = By.cssSelector("[data-testid='task-soft-select']");
    private final By saveBtn = By.cssSelector("[data-testid='task-save-btn']");
    
    private final By confirmDeleteBtn = By.cssSelector(".modal-content button.btn-primary");

    public TaskPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void create(String desc, int diff, int hours, Date start, Date end, String softwareName) {
        wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(descInput));
        
        driver.findElement(descInput).sendKeys(desc);
        driver.findElement(diffInput).sendKeys(String.valueOf(diff));
        driver.findElement(hoursInput).sendKeys(String.valueOf(hours));

        fillDate(startInput, start);
        fillDate(endInput, end);

        Select select = new Select(driver.findElement(softSelect));
        select.selectByVisibleText(softwareName);
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(descInput));
    }

    public void editDescription(String oldDesc, String newDesc) {
        WebElement row = getRow(oldDesc);
        row.findElement(By.cssSelector("[data-testid='edit-btn']")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(descInput));
        WebElement input = driver.findElement(descInput);
        input.clear();
        input.sendKeys(newDesc);
        
        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(descInput));
    }

    public void delete(String desc) {
        WebElement row = getRow(desc);
        row.findElement(By.cssSelector("[data-testid='delete-btn']")).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn)).click();
        wait.until(ExpectedConditions.invisibilityOf(row));
    }

    public boolean isVisible(String desc) {
        try {
            return !driver.findElements(By.cssSelector("[data-testid='row-" + desc + "']")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement getRow(String desc) {
        By loc = By.cssSelector("[data-testid='row-" + desc + "']");
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
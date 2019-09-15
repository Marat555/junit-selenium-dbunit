package com.maratbadykov.selenium.tests;

import com.github.mjeanroy.dbunit.core.annotations.DbUnitDataSet;
import com.github.mjeanroy.dbunit.core.annotations.DbUnitSetup;
import com.github.mjeanroy.dbunit.core.annotations.DbUnitTearDown;
import com.github.mjeanroy.dbunit.core.operation.DbUnitOperation;
import com.maratbadykov.selenium.DriverBase;
import com.maratbadykov.selenium.SeleniumRunner;
import com.maratbadykov.selenium.pageObjects.AuthPage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(SeleniumRunner.class)
public class CampaignsPageIT extends DriverBase {
    @BeforeClass
    public static void setUp() {
        instantiateDriverObject();
    }

    @AfterClass
    public static void tearDown() {
        closeDriverObjects();
    }

    @After
    public void afterEachMethod() {
        clearCookies();
    }

    private ExpectedCondition<Boolean> pageSizeEquals(final String size) {
        return driver -> driver.findElement(By.cssSelector("form#TemplateBackupForm > div:nth-of-type(2) > div:nth-of-type(2) > div > div:nth-of-type(2) > div:nth-of-type(4) > div > span:nth-of-type(2) > span > ul > li:nth-of-type(3) > a")).getText().equals(size);
    }

    @Test
    @DbUnitDataSet("/dbunit/xml/sampleData.xml")
    @DbUnitSetup(DbUnitOperation.INSERT)
    @DbUnitTearDown(DbUnitOperation.DELETE)
    public void pagingTest() throws Exception {
        WebDriver driver = getDriver();
        driver.get("https://10.20.3.138");
        AuthPage authPage = new AuthPage();
        authPage.fillLoginForm("default@user.com", "123").submitLogin();
        WebDriverWait wait = new WebDriverWait(driver, 30, 100);
        wait.until(pageSizeEquals("100"));
    }
}
package com.lucysecurity.lucy.pageObjects;

import com.lucysecurity.lucy.DriverBase;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

public class AuthPage {

    private Query loginFormInput = new Query().defaultLocator(By.name("LoginForm[email]"));
    private Query passwordFormInput = new Query().defaultLocator(By.name("LoginForm[password]"));
    private Query submitLoginFormButton = new Query().defaultLocator(By.className("btn"));

    public AuthPage() throws Exception {
        initQueryObjects(this, DriverBase.getDriver());
    }

    public AuthPage fillLoginForm(String login, String password) {
        loginFormInput.findWebElement().clear();
        loginFormInput.findWebElement().sendKeys(login);

        passwordFormInput.findWebElement().clear();
        passwordFormInput.findWebElement().sendKeys(password);

        return this;
    }

    public AuthPage submitLogin() {
        submitLoginFormButton.findWebElement().submit();

        return this;
    }
}
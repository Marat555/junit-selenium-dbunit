package com.lucysecurity.lucy;

import com.github.mjeanroy.dbunit.core.annotations.DbUnitConnection;
import com.github.mjeanroy.dbunit.integration.junit4.DbUnitJunitRunner;
import com.github.mjeanroy.dbunit.integration.junit4.DbUnitRule;
import com.lucysecurity.lucy.config.DriverFactory;
import com.lucysecurity.lucy.listeners.SeleniumRunListener;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DbUnitConnection(url = "jdbc:postgresql://localhost:5432/test", user = "deep", password = "123")
public class DriverBase {
    @Rule
    public DbUnitRule dbUnitRule = new DbUnitRule();

    private static List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<DriverFactory>());
    private static ThreadLocal<DriverFactory> driverFactoryThread;

    protected static void instantiateDriverObject() {
        driverFactoryThread = ThreadLocal.withInitial(() -> {
            DriverFactory driverFactory = new DriverFactory();
            webDriverThreadPool.add(driverFactory);
            return driverFactory;
        });
    }

    public static RemoteWebDriver getDriver() throws Exception {
        return driverFactoryThread.get().getDriver();
    }


    protected static void clearCookies() {
        try {
            driverFactoryThread.get().getStoredDriver().manage().deleteAllCookies();
        } catch (Exception ignored) {
            System.out.println("Unable to clear cookies, driver object is not viable...");
        }
    }

    protected static void closeDriverObjects() {
        for (DriverFactory driverFactory : webDriverThreadPool) {
            driverFactory.quitDriver();
        }
    }
}
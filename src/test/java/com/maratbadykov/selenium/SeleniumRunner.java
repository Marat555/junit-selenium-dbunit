package com.maratbadykov.selenium;

import com.maratbadykov.selenium.listeners.SeleniumRunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class SeleniumRunner extends BlockJUnit4ClassRunner {
 
    private SeleniumRunListener seleniumRunListener;
 
    public SeleniumRunner(Class cl) throws InitializationError {
        super(cl);
        seleniumRunListener = new SeleniumRunListener();
    }
 
     public void run(final RunNotifier notifier) {
         notifier.addListener(seleniumRunListener);
         super.run(notifier);
     }
}
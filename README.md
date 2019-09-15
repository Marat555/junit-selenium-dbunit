junit-selenium-dbunit
=======================

Maven template for automated testing with JUnit, Selenium and managing database state with DbUnit.

## DbUnit

For managing database state I used [dbunit-plus](https://github.com/mjeanroy/dbunit-plus).

Add database connection configuration to annotation @DbUnitConnection in DriverBase class.

Example:

```java
@DbUnitConnection(url = "jdbc:postgresql://localhost:5432/test", user = "deep", password = "123")
public class DriverBase {
  ...
}
```

Add file for inserting to database to /resources/dbunit/xml. Example of file:

```xml
	<?xml version="1.0" encoding="UTF-8"?>
    <dataset>
    	<person id="1" name="David"/>
    	<person id="2" name="George"/>
    	<person id="3" name="Freddy"/>
    	<person id="4" name="Steven"/>
    </dataset>
```

Here are the available annotations:

@DbUnitDataSet: define dataset (or directory containing dataset files) to load (can be used on package, entire class or a method).
@DbUnitInit: define SQL script to execute before any dataset insertion (can be used on package or entire class).
@DbUnitSetup: define DbUnit setup operation (can be used on package, entire class or a method).
@DbUnitTearDown: define DbUnit tear down operation (can be used on package, entire class or a method).

Example:

```java
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
```

##JUnit-Selenium 

As maven template, I used [Selenium-Maven-Template](https://github.com/Ardesco/Selenium-Maven-Template), but I made some changes in code and pom.xml replacing TestNg to JUnit, adding some and fixing dependencies conflicts in pom.xml.

1. Open a terminal window/command prompt
2. Clone this project.
3. `cd junit-selenium-dbunit` (Or whatever folder you cloned it into)
4. `mvn clean verify`

All dependencies should now be downloaded and the example google cheese test will have run successfully in headless mode (Assuming you have Firefox installed in the default location)

### What should I know?

- To run any unit tests that test your Selenium framework you just need to ensure that all unit test file names end, or start with "test" and they will be run as part of the build.
- The maven failsafe plugin has been used to create a profile with the id "selenium-tests".  This is active by default, but if you want to perform a build without running your selenium tests you can disable it using:

        mvn clean verify -P-selenium-tests
        
- The maven-failsafe-plugin will pick up any files that end in IT by default.  You can customise this is you would prefer to use a custom identifier for your Selenium tests.

### Known problems...

- It looks like SafariDriver is no longer playing nicely and we are waiting on Apple to fix it... Running safari driver locally in server mode and connecting to it like a grid seems to be the workaround.

### Anything else?

Yes you can specify which browser to use by using one of the following switches:

- -Dbrowser=firefox
- -Dbrowser=chrome
- -Dbrowser=ie
- -Dbrowser=edge
- -Dbrowser=opera

If you want to toggle the use of chrome or firefox in headless mode set the headless flag (by default the headless flag is set to true)

- -Dheadless=true
- -Dheadless=false

You don't need to worry about downloading the IEDriverServer, EdgeDriver, ChromeDriver , OperaChromiumDriver, or GeckoDriver binaries, this project will do that for you automatically.

You can specify a grid to connect to where you can choose your browser, browser version and platform:

- -Dremote=true 
- -DseleniumGridURL=http://{username}:{accessKey}@ondemand.saucelabs.com:80/wd/hub 
- -Dplatform=xp 
- -Dbrowser=firefox 
- -DbrowserVersion=44

You can even specify multiple threads (you can do it on a grid as well!):

- -Dthreads=2

You can also specify a proxy to use

- -DproxyEnabled=true
- -DproxyHost=localhost
- -DproxyPort=8080
- -DproxyUsername=fred
- -DproxyPassword=Password123

If the tests fail screenshots will be saved in ${project.basedir}/target/screenshots

If you need to force a binary overwrite you can do:

- -Doverwrite.binaries=true

### It's not working!!!

You have probably got outdated driver binaries, by default they are not overwritten if they already exist to speed things up.  You have two options:

- `mvn clean verify -Doverwrite.binaries=true`
- Delete the `selenium_standalone_binaries` folder in your resources directory
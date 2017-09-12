package org.daniel.github.test;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertTrue;
import static org.daniel.github.test.Selectors.*;
import static org.junit.Assert.assertFalse;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GithubTest {

    private final static String URL = "http://github.com/";

    private WebDriver driver;
    private WebDriverWait wait;
    private String username;
    private String password;
    private static final int EXPLICIT_WAIT_TIMEOUT_IN_SECONDS = 10;

    static final String TEST_REPO_NAME = "testRepoName";
    private static final String TEST_FILE = "testFile";
    private static final String TEST_FILE2 = "testFile2";
    private static final String TEST_PULL_REQUEST = "testPullRequest";

    @Before
    public void setUp() {
        driver = getWebdriver();
        try {
            driver.manage().window().maximize();
        } catch (WebDriverException e) { // maximize fails sometimes, but the test can continue without it
            System.out.println(e.getMessage());
        }
        driver.get(URL);
        wait = new WebDriverWait(driver, EXPLICIT_WAIT_TIMEOUT_IN_SECONDS);
        username = System.getProperty("username");
        password = System.getProperty("password");
        login();
    }

    private WebDriver getWebdriver() {
        String browserStr = System.getProperty("browser");
        switch (browserStr) {
            case "firefox": return new FirefoxDriver();
            case "chrome": return new ChromeDriver();
            default: throw new RuntimeException("Unsupported browser: " + browserStr);
        }
    }

    private void login() {
        driver.findElement(Selectors.SIGN_IN_LINK).click();
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(SIGN_IN_BUTTON).click();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test1CreateRepository() {

        driver.findElement(CREATE_NEW_BUTTON).click();
        driver.findElement(CREATE_NEW_REPOSITORY_BUTTON).click();

        driver.findElement(REPOSITORY_NAME_INPUT).sendKeys(TEST_REPO_NAME);
        driver.findElement(INIT_REPO_CHECKBOX).click();
        wait.until(elementToBeClickable(CREATE_REPOSITORY_BUTTON));
        driver.findElement(CREATE_REPOSITORY_BUTTON).click();

        // if browser redirected to a url with repository name, I assume that repository was created
        wait.until(driver -> driver.getCurrentUrl().endsWith(TEST_REPO_NAME) || driver.getCurrentUrl().endsWith(TEST_REPO_NAME + "/"));
        assertFalse(driver.findElements(REPO_LINK).isEmpty());
    }

    @Test
    public void test2PushCommit() {
        driver.findElement(By.linkText(TEST_REPO_NAME)).click();
        createNewFile(TEST_FILE);

        wait.until(elementToBeClickable(SUBMIT_NEW_FILE_BUTTON));
        driver.findElement(SUBMIT_NEW_FILE_BUTTON).click();

        driver.findElement(COMMITS_TAB).click();

        assertFalse("No commits created", driver.findElements(By.cssSelector("a[title='Create " + TEST_FILE + "'")).isEmpty());

    }

    @Test
    public void test3CreatePullRequest() throws InterruptedException {
        driver.findElement(By.linkText(TEST_REPO_NAME)).click();
        createNewFile(TEST_FILE2);

        driver.findElement(CREATE_PULL_REQUEST_RADIO_OPTION).click();
        driver.findElement(CREATE_PULL_REQUEST_NAME_INPUT).sendKeys(TEST_PULL_REQUEST);
        wait.until(elementToBeClickable(SUBMIT_NEW_FILE_BUTTON));
        driver.findElement(SUBMIT_NEW_FILE_BUTTON).click();
        driver.findElement(CREATE_PULL_REQUEST_BUTTON).click();
        assertFalse("Failed to create pull request", driver.findElements(MERGE_WINDOW).isEmpty());
    }

    @Test
    public void test4AcceptPullRequest() throws InterruptedException {
        driver.findElement(By.linkText(TEST_REPO_NAME)).click();
        driver.findElement(PULL_REQUESTS_TAB).click();
        wait.until(presenceOfElementLocated(PULL_REQUEST_ENTRY));
        driver.findElement(PULL_REQUEST_ENTRY).click();
        wait.until(presenceOfElementLocated(ACCEPT_PULL_REQUEST_BUTTON));
        driver.findElement(ACCEPT_PULL_REQUEST_BUTTON).click();
        wait.until(presenceOfElementLocated(CONFIRM_ACCEPT_PULL_REQUEST_BUTTON));
        driver.findElement(CONFIRM_ACCEPT_PULL_REQUEST_BUTTON).click();
        wait.until(driver -> { // there is some quirk on this page - sometimes it is not updated, so I will refresh
            driver.navigate().refresh();
            return !driver.findElements(MERGED_PURPLE_ICON).isEmpty();
        });
    }

    @Test
    public void test5DeleteRepository() throws InterruptedException {
        driver.findElement(By.linkText(TEST_REPO_NAME)).click();
        driver.findElement(SETTINGS_TAB).click();
        wait.until(elementToBeClickable(REMOVE_REPO_BUTTON));
        driver.findElement(REMOVE_REPO_BUTTON).click();
        wait.until(visibilityOfElementLocated(DELETE_REPO_NAME_INPUT));
        driver.findElement(DELETE_REPO_NAME_INPUT).sendKeys(TEST_REPO_NAME);
        wait.until(elementToBeClickable(DELETE_REPO_CONFIRM_BUTTON));
        driver.findElement(DELETE_REPO_CONFIRM_BUTTON).click();
        // after deleting should be redirected to the main page
        wait.until(driver -> {
            String url = driver.getCurrentUrl();
            return url.endsWith("github.com/") || url.endsWith("github.com"); // could have trailing slash optionally
        });
        assertTrue(driver.findElements(By.linkText(TEST_REPO_NAME)).isEmpty());
    }

    private void createNewFile(String filename) {
        String createNewFileButtonFormAction = "/" + username + "/" + TEST_REPO_NAME + "/new/master";
        String createNewFileButtonCssSelector = "form[action='" + createNewFileButtonFormAction + "']>button";
        wait.until(presenceOfElementLocated(By.cssSelector(createNewFileButtonCssSelector)));
        driver.findElement(By.cssSelector(createNewFileButtonCssSelector)).click();
        driver.findElement(By.name("filename")).sendKeys(filename);
    }

}
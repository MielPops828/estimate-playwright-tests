package ui.tests;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserType;
import helpers.ParametersProvider;
import lombok.Getter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    @Getter
    protected Page page;
    protected SoftAssert softAssert;

    @BeforeMethod
    public void setup() throws IOException {
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.
                LaunchOptions()
                .setHeadless(isHeadless));
        browserContext = browser.newContext(new Browser
                .NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("ru-RU")
                .setBaseURL(resolveWebUrl()));
        page = browserContext.newPage();
        softAssert = new SoftAssert();
    }

    @AfterMethod
    public void tearDown() {
        page.close();
        browserContext.close();
        browser.close();
        playwright.close();
    }

    protected String resolveWebUrl() throws IOException {
        String sysUrl = System.getProperty("webUrl");
        if (sysUrl != null && !sysUrl.isEmpty()) {
            return sysUrl;
        }
        return ParametersProvider.getProperty("webUrl");
    }
}

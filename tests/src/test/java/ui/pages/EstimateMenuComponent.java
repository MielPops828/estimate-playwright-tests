package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class EstimateMenuComponent extends BasePage {

    private final Locator estimateLinkLocator = page.locator("md-sidenav a:has-text(\"Оценки\")");

    public EstimateMenuComponent(Page page) {
        super(page);
    }

    @Step("Открыть страницу с оценками")
    public EstimatesPage openEstimates() {
        estimateLinkLocator.click();
        return new EstimatesPage(page);
    }
}

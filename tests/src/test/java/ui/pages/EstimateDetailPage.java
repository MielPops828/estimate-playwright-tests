package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class EstimateDetailPage extends BasePage{

    private final Locator sideNavButton = page.locator("#toggle-side-nav-button");
    private final Locator editPhaseButton = page.locator("#edit-phase-button");
    private final Locator confirmDeletePhaseButton = page.locator("button.md-primary.md-button.md-default-theme.md-ink-ripple");
    private final Locator addPhaseButton = page.locator("#add-phase-button");
    private final Locator parentElement = page.locator("md-tab-item.md-active label.phase-name");

    public EstimateDetailPage(Page page) {
        super(page);
    }

    @Step("Нажать на бургер-меню")
    public EstimateMenuComponent openNavMenu() {
        sideNavButton.click();
        return new EstimateMenuComponent(page);
    }

    @Step("Нажать на кнопку редактирования выбранной фазы")
    public EstimateDetailPage editPhase(String phase) {
        Locator mobileAppTab = page.locator("md-pagination-wrapper md-tab-item")
                .filter(new Locator.FilterOptions().setHasText(phase));
        mobileAppTab.click();
        editPhaseButton.click();
        mobileAppTab.locator("button.tab-icon-cancel").click();
        return this;
    }

    @Step("Подтвержить удаление выбранной фазы")
    public EstimateDetailPage confirmDeletePhase() {
        confirmDeletePhaseButton.filter(new Locator.FilterOptions().setHasText("ОК")).click();
        return new EstimateDetailPage(page);
    }

    @Step("Нажать на кнопку 'Создать фазу'")
    public EstimatePhaseComponent clickAddPhase() {
        addPhaseButton.click();
        return new EstimatePhaseComponent(page);
    }

    @Step("Проверить существования фазы у оценки")
    public Locator getPhaseNameLabel(String phase) {
        return parentElement;
    }
}

package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class EstimatePhaseComponent extends BasePage{

    private final Locator closeComponent = page.locator("[ng-click='vm.closeModal()']");
    private final Locator savePhaseButton = page.locator(".md-primary.md-button.md-ink-ripple");
    private final Locator phaseNameInput = page.locator("#save-adding-phases-button");

    public EstimatePhaseComponent(Page page) {
        super(page);
    }

    @Step("Закрыть модальное окно добавления фазы")
    public EstimateDetailPage closePhaseComponent() {
        closeComponent.click();
        return new EstimateDetailPage(page);
    }

    @Step("Занолнить поле ввода значением: {phase}")
    public EstimatePhaseComponent inputCustomPhase(String phase) {
        phaseNameInput.fill(phase);
        return this;
    }

    @Step("Нажать на кнопку 'Сохранить'")
    public EstimateDetailPage clickSavePhaseButton() {
        savePhaseButton.click();
        return new EstimateDetailPage(page);
    }
}

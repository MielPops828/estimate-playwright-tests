package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class EstimateEditPage extends BasePage{

    private final Locator clientInput = page.locator("#input_4");
    private final Locator projectInput = page.locator("#input_5");
    private final Locator descriptionInput = page.locator("#input_7");
    private final Locator searchExpertInput = page.locator("#input-11");
    private final Locator ruRadioButton = page.locator("#input-272");
    private final Locator crmInput = page.locator("textarea.md-input[name=\"linkToCRM\"]");
    private final Locator qaCheckbox = page.getByRole(AriaRole.CHECKBOX,
            new Page.GetByRoleOptions().setName("*Направление QA"));
    private final Locator saveButton = page.locator("#edit-about-button");
    private final Locator addPhaseButton = page.locator("#add-phase-button");

    public EstimateEditPage(final Page page) {
        super(page);
    }

    @Step("Заполнить поля \"Клиент\", \"Название проекта\", \"Описание\", \"Ссылка в CRM\"")
    public EstimateEditPage fillEditEstimateFields(String client, String project,
                                                   String description, String crmLink) {
        clientInput.fill(client);
        projectInput.fill(project);
        descriptionInput.fill(description);
        crmInput.fill(crmLink);
        return this;
    }

    @Step("Выбрать эксперта: {expertName}")
    public EstimateEditPage addExpert(String expertName) {
        searchExpertInput.fill(expertName);
        page.keyboard().press("Enter");
        return this;
    }

    @Step("Выбрать отдел: Направление QA")
    public EstimateEditPage selectQaCheckbox() {
        qaCheckbox.click();
        return this;
    }

    @Step("Выбрать языковую настройку оценки: RU")
    public EstimateEditPage selectRuLanguage() {
        ruRadioButton.click();
        return this;
    }

    @Step("Нажать на кнопку 'Сохранить и добавить фазу'")
    public EstimatePhaseComponent clickSaveAndAddPhase() {
        saveButton.click();
        return new EstimatePhaseComponent(page);
    }

    @Step("Нажать на кнопку 'Создать фазу'")
    public EstimateEditPage clickAddPhase() {
        addPhaseButton.click();
        return this;
    }
}

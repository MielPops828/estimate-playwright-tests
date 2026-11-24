package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

public class EstimatesPage extends BasePage {

    public static final String PAGE_URL = "/estimates";
    private final String nameLocator = ".task-item-name";
    private final String departmentLocator = ".department-item-name";
    private final Locator addEstimateButton = page.locator("#open-menu-button");
    private final Locator clickLanguage = page.locator(".adding-area-item").first();
    private final String deleteEstimate = "#disable-deletion-button";
    private final String confirmAlert = "button[md-autofocus]";

    public EstimatesPage(final Page page) {
        super(page);
    }

    @Step("Получение url страницы")
    public static String getPageUrl() {
        return PAGE_URL;
    }

    @Step("Нажать на кнопку 'Создать оценку'")
    public EstimatesPage clickAddEstimateButton() {
        addEstimateButton.click();
        return this;
    }

    @Step("Выбрать язык")
    public EstimateEditPage clickLanguageButton() {
        clickLanguage.click();
        return new EstimateEditPage(page);
    }

    public Locator getEstimateByNameAndDepartment(String name, String department) {
        return page.locator("a.task-item")
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator(".task-item-name")
                                .filter(new Locator.FilterOptions().setHasText(name))))
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator(".department-item-name")
                                .filter(new Locator.FilterOptions().setHasText(department))));
    }
    @Step("Получить клиента текущей оценки")
    public Locator getNameInFindRow(Locator row) {
        return row != null ? row.locator(nameLocator) : null;
    }

    @Step("Получить название проекта текущей оценки")
    public Locator getDepartmentInFindRow(Locator row) {
        return row != null ? row.locator(departmentLocator).first() : null;
    }

    @Step("Удалить выбранную оценку")
    public EstimatesPage deleteEstimate(Locator row) {
        row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        row.locator(deleteEstimate).click();
        return this;
    }

    @Step("Подтвердить удаление оценки")
    public EstimatesPage confirmDeleteEstimate(Locator row) {
        page.locator(confirmAlert).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(confirmAlert).click();
        row.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        return this;
    }

    @Step("Нажать на оценку")
    public EstimateDetailPage clickEstimate(Locator findRow) {
        if (findRow != null) {
            findRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            findRow.click();
            return new EstimateDetailPage(page);
        }
        return null;
    }
}

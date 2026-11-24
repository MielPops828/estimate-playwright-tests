package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.Objects;

public class LoginPage extends BasePage{

    private static final String PAGE_URL = "/login";

    private final Locator loginInput = page.locator("input[ng-model='vm.login']");
    private final Locator passwordInput = page.locator("input[ng-model='vm.password']");
    private final Locator loginButton = page.locator("button[ng-click='vm.log()']");
    private final Locator errorText = page.locator("div.error-msg");

    public LoginPage(final Page page) {
        super(page);
    }

    @Step("Открыть страницу: " + PAGE_URL)
    public LoginPage openPage() {
        page.navigate(PAGE_URL);
        return this;
    }

    @Step("Заполнить поле 'Login'")
    public final LoginPage sendLogin(String login){
        loginInput.fill(login);
        return this;
    }

    @Step("Заполнить поле 'Password'")
    public final LoginPage sendPassword(String password){
        passwordInput.fill(password);
        return this;
    }

    @Step("Нажать на кнопку 'Login'")
    public final LoginPage loginButtonClick(){
        loginButton.click();
        return this;
    }

    @Step("Переход на главную страницу после успешной авторизации")
    public final EstimatesPage login() throws IOException {
        loginButtonClick();
        return new EstimatesPage(page);
    }

    @Step("Получить указанное в поле 'Login' значение")
    public String getLoginFieldText() {
        return loginInput.inputValue();
    }

    @Step("Получить указанное в поле 'Password' значение")
    public String getPasswordFieldText() {
        return passwordInput.inputValue();
    }

    @Step("Проверить отображение сообщения: {text}")
    public boolean isErrorMessageDisplayed(String text){
        try {
            errorText.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));
            return text.contains(Objects.requireNonNull(errorText.textContent()));
        } catch (TimeoutError e) {
            return false;
        }
    }
}

package ui.tests;

import java.io.IOException;

import helpers.ParametersProvider;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import ui.pages.EstimatesPage;
import ui.pages.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Автоматизация страницы Estimate App")
@Feature("Страница авторизации")
public class LoginTest extends BaseTest{

    @Severity(SeverityLevel.CRITICAL)
    @Story("QAA-1. Тест авторизации с корректным логином и паролем")
    @Test
    public void checkCorrectLoginPassAuth() throws IOException {
        String login = ParametersProvider.getProperty("adminLogin");
        String password = ParametersProvider.getProperty("adminPassword");
        LoginPage loginPage = new LoginPage(getPage())
                .openPage()
                .sendLogin(login)
                .sendPassword(password)
                .loginButtonClick();
        assertThat(loginPage.getPage()).hasURL(EstimatesPage.getPageUrl());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("QAA-2. Авторизация с несуществующим логином и паролем")
    @Test
    public void checkIncorrectLoginPassAuth() throws IOException {
        String login = ParametersProvider.getProperty("adminInvalidLogin");
        String password = ParametersProvider.getProperty("adminInvalidPassword");
        LoginPage loginPage = new LoginPage(getPage())
                .openPage()
                .sendLogin(login)
                .sendPassword(password)
                .loginButtonClick();
        softAssert.assertFalse(loginPage.getPage().url().contains(EstimatesPage.getPageUrl()));
        softAssert.assertTrue(loginPage.isErrorMessageDisplayed("User not found"));
        softAssert.assertAll();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("QAA-3. Авторизация с корректным логином и пустым паролем")
    @Test
    public void checkAuthWithEmptyPass() throws IOException {
        String login = ParametersProvider.getProperty("adminLogin");
        LoginPage loginPage = new LoginPage(getPage())
                .openPage()
                .sendLogin(login)
                .loginButtonClick();
        softAssert.assertFalse(loginPage.getPage().url().contains(EstimatesPage.getPageUrl()));
        softAssert.assertEquals(loginPage.getLoginFieldText(), login);
        softAssert.assertEquals(loginPage.getPasswordFieldText(), "");
        softAssert.assertAll();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("QAA-4. Авторизация с пустым логином и паролем")
    @Test
    public void checkAuthWithEmptyLoginAndPass() {
        LoginPage loginPage = new LoginPage(getPage())
                .openPage()
                .loginButtonClick();
        softAssert.assertFalse(loginPage.getPage().url().contains(EstimatesPage.getPageUrl()));
        softAssert.assertEquals(loginPage.getLoginFieldText(), "");
        softAssert.assertEquals(loginPage.getPasswordFieldText(), "");
        softAssert.assertAll();
    }
}

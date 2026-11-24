package ui.tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.EstimateCreator;
import helpers.ParametersProvider;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ui.pages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Автоматизация страницы Estimate App")
@Feature("Страница 'Оценки'")
public class EstimateTest extends BaseTest{

    private final List<String> createdEstimateHrefs = new ArrayList<>();

    @BeforeMethod
    public void auth() throws IOException {
        String login = ParametersProvider.getProperty("adminLogin");
        String password = ParametersProvider.getProperty("adminPassword");
        LoginPage loginPage = new LoginPage(getPage())
                .openPage()
                .sendLogin(login)
                .sendPassword(password)
                .loginButtonClick();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("QAA-5. Создание оценки")
    @Test
    public void testCreateEstimate() throws IOException {
        String estimateClient = "test_" + System.currentTimeMillis();
        String projectName = "test project_" + System.currentTimeMillis();
        EstimatesPage estimatesPage = new EstimateCreator().createEstimate(
                page, estimateClient, projectName, ParametersProvider.getProperty("crmLink"),
                ParametersProvider.getProperty("estimateDescription"),
                ParametersProvider.getProperty("expertName")
        )
                .closePhaseComponent()
                .openNavMenu()
                .openEstimates();
        Locator tableRow = estimatesPage.getEstimateByNameAndDepartment(estimateClient, projectName);
        assertThat(tableRow).hasCount(1);
        String href = tableRow.first().getAttribute("href");
        createdEstimateHrefs.add(href);
        softAssert.assertEquals(estimatesPage.getNameInFindRow(tableRow).textContent(), estimateClient, "Оценка не содержит ожидаемое название клиента");
        softAssert.assertEquals(estimatesPage.getDepartmentInFindRow(tableRow).textContent(), projectName, "Оценка не содержит ожидаемое название проекта");
        softAssert.assertAll();
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("QAA-6. Удаление оценки")
    @Test
    public void testDeleteEstimate() throws IOException {
        String estimateClient = "test_" + System.currentTimeMillis();
        String projectName = "test project_" + System.currentTimeMillis();
        EstimatesPage estimatesPage = new EstimateCreator().createEstimate(
                page, estimateClient, projectName, ParametersProvider.getProperty("crmLink"),
                ParametersProvider.getProperty("estimateDescription"),
                ParametersProvider.getProperty("expertName")
        )
                .closePhaseComponent()
                .openNavMenu()
                .openEstimates();
        Locator tableRow = estimatesPage.getEstimateByNameAndDepartment(estimateClient, projectName);
        assertThat(tableRow).hasCount(1);
        String href = tableRow.first().getAttribute("href");
        createdEstimateHrefs.add(href);
        estimatesPage.deleteEstimate(tableRow).confirmDeleteEstimate(tableRow);
        assertThat(tableRow).hasCount(0);
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("QAA-8. Добавление катомной фазы не из справочника")
    @Test
    public void testCreateCustomEstimatePhase() throws IOException {
        String phase = ParametersProvider.getProperty("phaseName");
        String estimateClient = "test_" + System.currentTimeMillis();
        String projectName = "test project_" + System.currentTimeMillis();
        EstimatesPage estimatesPage = new EstimateCreator().createEstimate(
                        page, estimateClient, projectName, ParametersProvider.getProperty("crmLink"),
                        ParametersProvider.getProperty("estimateDescription"),
                        ParametersProvider.getProperty("expertName")
                )
                .closePhaseComponent()
                .openNavMenu()
                .openEstimates();
        Locator tableRow = estimatesPage.getEstimateByNameAndDepartment(estimateClient, projectName);
        assertThat(tableRow).hasCount(1);
        String href = tableRow.first().getAttribute("href");
        createdEstimateHrefs.add(href);
        Locator phaseNameLocator = estimatesPage.clickEstimate(tableRow)
                .clickAddPhase()
                .inputCustomPhase(phase)
                .clickSavePhaseButton()
                .getPhaseNameLabel(phase);
        assertThat(phaseNameLocator).hasText(phase);
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("QAA-20. Удаление фазы")
    @Test
    public void testDeletePhase() throws IOException {
        String phase = ParametersProvider.getProperty("phaseName");
        String estimateClient = "test_" + System.currentTimeMillis();
        String projectName = "test project_" + System.currentTimeMillis();
        EstimateDetailPage estimatePage = new EstimateCreator().createEstimate(
                        page, estimateClient, projectName, ParametersProvider.getProperty("crmLink"),
                        ParametersProvider.getProperty("estimateDescription"),
                        ParametersProvider.getProperty("expertName")
                )
                .inputCustomPhase(phase)
                .clickSavePhaseButton()
                .editPhase(phase)
                .confirmDeletePhase();
        assertThat(estimatePage.getPhaseNameLabel(phase)).hasCount(0);
        Locator tableRow = estimatePage.openNavMenu().openEstimates().getEstimateByNameAndDepartment(estimateClient, projectName);
        assertThat(tableRow).hasCount(1);
        String href = tableRow.first().getAttribute("href");
        createdEstimateHrefs.add(href);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupCreatedEstimates() {
        page.navigate("/estimates");
        page.locator("a.task-item").first().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        for (String href : createdEstimateHrefs) {
            try {
                Locator row = page.locator("a.task-item[href='" + href + "']");
                if (row.count() == 0) {
                    continue;
                }
                EstimatesPage estimatesPage = new EstimatesPage(page).deleteEstimate(row).confirmDeleteEstimate(row);
                row.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.DETACHED)
                        .setTimeout(10000));
            } catch (Exception e) {
                System.err.println("Cleanup failed for href=" + href + ": " + e.getMessage());
            }
        }
        createdEstimateHrefs.clear();
    }
}

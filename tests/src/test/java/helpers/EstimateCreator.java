package helpers;

import com.microsoft.playwright.Page;
import ui.pages.EstimatePhaseComponent;
import ui.pages.EstimatesPage;

public class EstimateCreator {
    public EstimatePhaseComponent createEstimate(Page page, String estimateClient, String projectName,
                                                 String estimateDescription, String crmLink, String expertName) {
        return new EstimatesPage(page)
                .clickAddEstimateButton()
                .clickLanguageButton()
                .fillEditEstimateFields(
                        estimateClient,
                        projectName,
                        estimateDescription,
                        crmLink
                )
                .addExpert(expertName)
                .selectQaCheckbox()
                .clickSaveAndAddPhase();
    }
}

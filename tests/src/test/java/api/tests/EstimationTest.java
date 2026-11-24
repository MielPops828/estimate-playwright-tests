package api.tests;

import api.TestEstimateAPI;
import api.models.EstimateRequest;
import api.models.EstimateResponse;
import api.utills.Steps;
import com.microsoft.playwright.APIResponse;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.io.IOException;

@Epic("Автоматизация в приложении Estimate App")
@Feature("Создание оценки")
public class EstimationTest extends TestEstimateAPI {

    @Severity(SeverityLevel.CRITICAL)
    @Story("Успешное создание новой оценки для русскоязычного клиента")
    @Test
    public void testSuccessCreateEstimate() throws IOException {
        APIResponse response = post("/api/estimates", new EstimateRequest("{ru}"));
        softAssert.assertEquals(200, response.status());

        EstimateResponse estimateResponse = parseResponse(response, EstimateResponse.class);
        String estimateId = estimateResponse.getId();
        estimatesId.add(estimateId);

        softAssert.assertNotNull(estimateId);
        softAssert.assertEquals(estimateResponse.getName(), "Новая оценка");
        softAssert.assertEquals(estimateResponse.getStatus().getValue(), "new");
        softAssert.assertEquals(estimateResponse.getStatus().getName(), "Новая");
        softAssert.assertAll();
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Успешное копирование оценки")
    @Test
    public void testCopyEstimate() throws IOException {
        String estimateId = new Steps().createEstimation();
        APIResponse response = postCopy("/api/estimates/" + estimateId + "/copy");
        softAssert.assertEquals(200, response.status());
        estimatesId.add(estimateId);

        EstimateResponse estimateResponse = parseResponse(response, EstimateResponse.class);

        softAssert.assertNotNull(estimateResponse.getId());
        softAssert.assertEquals(estimateResponse.getOriginalId(), estimateId);
        softAssert.assertEquals(estimateResponse.getName(), "Новая оценка - копия");
        softAssert.assertEquals(estimateResponse.getLocale(), "{ru}");
        softAssert.assertEquals(estimateResponse.getStatus().getValue(), "inprogress");
        softAssert.assertEquals(estimateResponse.getStatus().getName(), "В процессе");

        APIResponse getResponse = get("/api/estimates/" + estimateResponse.getId());
        softAssert.assertEquals(200, getResponse.status(), "GET copy failed: " + getResponse.text());
        EstimateResponse copiedEstimate = parseResponse(getResponse, EstimateResponse.class);
        estimatesId.add(estimateResponse.getId());

        softAssert.assertEquals(copiedEstimate.getId(), estimateResponse.getId());
        softAssert.assertEquals(copiedEstimate.getName(), estimateResponse.getName());
        softAssert.assertEquals(copiedEstimate.getLocale(), estimateResponse.getLocale());
        softAssert.assertEquals(copiedEstimate.getStatus().getValue(), estimateResponse.getStatus().getValue());
        softAssert.assertEquals(copiedEstimate.getStatus().getName(), estimateResponse.getStatus().getName());
        softAssert.assertAll();
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Успешное удаление оценки")
    @Test
    public void testDeleteEstimate() throws IOException {
        String estimateId = new Steps().createEstimation();
        APIResponse response = delete("/api/estimates/" + estimateId);

        softAssert.assertEquals(200, response.status());
        softAssert.assertEquals(estimateId + " removed", response.text());

        APIResponse responseGet = get("/api/estimates");

        softAssert.assertFalse(responseGet.text().contains(estimateId), "This estimate was not deleted");
        softAssert.assertAll();
    }
}

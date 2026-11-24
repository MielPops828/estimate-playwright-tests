package api.utills;

import api.TestEstimateAPI;
import api.models.EstimateRequest;
import api.models.EstimateResponse;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;

public class Steps extends TestEstimateAPI {
    public String createEstimation() {
        APIResponse response = post("/api/estimates", new EstimateRequest("{ru}"));
        Assert.assertEquals(response.status(), 200);
        EstimateResponse estimateResponse = parseResponse(response, EstimateResponse.class);
        return estimateResponse.getId();
    }
}

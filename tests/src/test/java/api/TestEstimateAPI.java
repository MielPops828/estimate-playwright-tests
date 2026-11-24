package api;

import api.models.LoginRequest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import helpers.ParametersProvider;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestEstimateAPI {

    private Playwright playwright;
    private static APIRequestContext request;

    protected SoftAssert softAssert;
    protected List<String> estimatesId;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        estimatesId = new ArrayList<>();
    }

    @BeforeMethod
    public void createAPIRequestContext() throws IOException {
        softAssert = new SoftAssert();
        LoginRequest login = new LoginRequest(ParametersProvider.getProperty("adminLogin"),
                ParametersProvider.getProperty("adminPassword"));

        APIRequestContext loginContext = playwright.request().newContext();
        APIResponse loginResponse = loginContext.post(resolveWebUrl() + "/api/auth/login",
                RequestOptions.create().setData(login));

        if (!loginResponse.ok()) {
            throw new IllegalStateException(
                    "Login failed: " + loginResponse.status() + " " + loginResponse.text());
        }

        String token = objectMapper.readTree(loginResponse.text()).get("token").asText();

        loginContext.dispose();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(resolveWebUrl())
                        .setExtraHTTPHeaders(headers)
        );
    }

    @AfterMethod
    public void tearDownRequestContext() {
        if (estimatesId != null && !estimatesId.isEmpty()) {
            for (String id : estimatesId) {
                if (id != null) {
                    delete("/api/estimates/" + id);
                }
            }
            estimatesId.clear();
        }
        if (request != null) {
            request.dispose();
            request = null;
        }
    }

    @AfterClass
    public void tearDownPlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    protected <T> T parseResponse(APIResponse response, Class<T> apiClass) {
        try {
            return objectMapper.readValue(response.text(), apiClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response: " + response.text() + " | cause: " + e, e);
        }
    }

    protected APIResponse get(String url) {
        return request.get(url);
    }

    protected static APIResponse post(String url, Object data) {
        return request.post(url, RequestOptions.create().setData(data));
    }

    protected static APIResponse postCopy(String url) {
        return request.post(url);
    }

    protected APIResponse delete(String url) {
        return request.delete(url);
    }

    private String resolveWebUrl() throws IOException {
        String sysUrl = System.getProperty("webUrl");
        if (sysUrl != null && !sysUrl.isEmpty()) {
            return sysUrl;
        }
        return ParametersProvider.getProperty("webUrl");
    }
}

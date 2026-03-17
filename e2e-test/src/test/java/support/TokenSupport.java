package support;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class TokenSupport {

    private static final Playwright playwright = Playwright.create();

    public static String fetchUserToken() {
        APIRequestContext api = playwright.request().newContext();

        APIResponse response = api.post(
                "https://hkstwk-dev.eu.auth0.com/oauth/token",
                RequestOptions.create().setData(Map.of(
                        "grant_type", "password",
                        "username", "testuser@example.com",
                        "password", "x38HMsFd&is!7?@p",
                        "audience", "http://localhost:8080",
                        "client_id", "LOjBkTo8Sb2kdIVhkUbA431RaESKFlWd",
                        "client_secret", "OdPvLFhInYI-ep_-PzgfCoDL2rYYkPBQx2mLzHqv-6k-kW8DuB5_gSFPKpU9c_Ew",
                        "scope", "openid profile email"
                ))
        );
        System.out.println(response.text());

        if (!response.ok()) {
            throw new RuntimeException("Failed to fetch Auth0 user token: " + response.status());
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.text());
            return json.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Auth0 token JSON", e);
        }
    }
}

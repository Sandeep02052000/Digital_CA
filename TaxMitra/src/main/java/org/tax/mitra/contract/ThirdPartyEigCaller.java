package org.tax.mitra.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThirdPartyEigCaller {

    private final RestTemplate restTemplate;
    private final ThirdPartyMockProvider mockProvider;
    private boolean mockEnabled;

    @Autowired
    public ThirdPartyEigCaller(RestTemplate restTemplate,
                               ThirdPartyMockProvider mockProvider,
                               @Value("${thirdparty.mock-enabled:true}") boolean mockEnabled) {
        this.restTemplate = restTemplate;
        this.mockProvider = mockProvider;
        this.mockEnabled = mockEnabled;
    }

    public ResponseEntity<Map> execute(
            String url,
            HttpMethod method,
            Map<String, String> headers,
            Map<String, String> queryParams,
            Object requestBody
    ) {
        if (mockEnabled) {
            Map<String,Object> mockBody = mockProvider.getByGstinId(queryParams);
            @SuppressWarnings("unchecked")
            ResponseEntity<Map> response = ResponseEntity.ok(mockBody);
            return response;
        }
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            if (queryParams != null && !queryParams.isEmpty()) {
                queryParams.forEach(builder::queryParam);
            }
            String finalUrl = builder.toUriString();
            HttpHeaders httpHeaders = new HttpHeaders();
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(httpHeaders::set);
            }
            HttpEntity<?> entity;
            if (requestBody != null) {
                entity = new HttpEntity<>(requestBody, httpHeaders);
            } else {
                entity = new HttpEntity<>(httpHeaders);
            }
            ResponseEntity<Map> response = restTemplate.exchange(
                    finalUrl,
                    method,
                    entity,
                    Map.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(
                        String.format("Third-party API returned non-success status: %s, URL: %s",
                                response.getStatusCode(), finalUrl)
                );
            }
            return response;
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            String errorBody = ex.getResponseBodyAsString();
            throw new RuntimeException(
                    String.format("API error | URL: %s | Status: %s | Response: %s",
                            url, ex.getStatusCode(), errorBody),
                    ex
            );
        } catch (org.springframework.web.client.ResourceAccessException ex) {
            throw new RuntimeException(
                    String.format("API connection error or timeout | URL: %s | Message: %s",
                            url, ex.getMessage()),
                    ex
            );
        } catch (Exception ex) {
            throw new RuntimeException(
                    String.format("Unexpected error while calling API | URL: %s | Error: %s",
                            url, ex.getMessage()),
                    ex
            );
        }
    }

    public ResponseEntity<Map> get(String url, Map<String, String> queryParams) {
        return execute(url, HttpMethod.GET, getHeaders(), queryParams, null);
    }

    public ResponseEntity<Map> post(String url, Object body) {
        return execute(url, HttpMethod.POST, getHeaders(), null, body);
    }

    private Map<String,String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
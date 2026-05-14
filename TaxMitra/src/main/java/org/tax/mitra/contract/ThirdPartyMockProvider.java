package org.tax.mitra.contract;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class ThirdPartyMockProvider {

    private final ObjectMapper objectMapper;

    private List<Object> cache = new ArrayList<>();

    public ThirdPartyMockProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadMockData() {
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("mock/gstinResponseMock.json");

            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Object.class);

            cache = objectMapper.readValue(is, type);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load GST mock file", e);
        }
    }

    public List<Object> getAll() {
        return cache;
    }

    public Map<String, Object> getByGstinId(Map<String, String> requestBody) {
        String gstin = requestBody.get("gstin");
        if (gstin == null || gstin.isEmpty()) {
            throw new RuntimeException("GSTIN is missing in request body");
        }
        for (Object obj : cache) {
            Map<String, Object> response = (Map<String, Object>) obj;
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data != null && gstin.equals(data.get("gstin"))) {
                return response;
            }
        }
        throw new RuntimeException("No mock data found for GSTIN: " + gstin);
    }
}
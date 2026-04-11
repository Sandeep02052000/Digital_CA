package org.tax.mitra.cache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tax.mitra.entity.SystemPreference;
import org.tax.mitra.repository.SystemPreferenceRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SystemPreferenceCache {

    private final SystemPreferenceRepository repository;
    private final Map<String, SystemPreference> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadCache() {
        List<SystemPreference> preferences = repository.findAll();
        for (SystemPreference pref : preferences) {
            cache.put(pref.getPreferenceCode(), pref);
        }
    }

    public SystemPreference get(String code) {
        return cache.get(code);
    }

    public String getValue(String code) {
        SystemPreference pref = cache.get(code);
        return pref != null ? pref.getPreferenceValue() : null;
    }

    public boolean isEnabled(String code) {
        SystemPreference pref = cache.get(code);
        return pref != null && "ACTIVE".equalsIgnoreCase(pref.getStatus());
    }

    public void refresh() {
        cache.clear();
        loadCache();
    }
}
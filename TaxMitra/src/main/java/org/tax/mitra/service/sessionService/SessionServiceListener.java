package org.tax.mitra.service.sessionService;

import org.tax.mitra.model.ValidateSessionRequest;

public interface SessionServiceListener {
    void validateSession(ValidateSessionRequest requestModel);
}

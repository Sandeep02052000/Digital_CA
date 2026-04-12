package org.tax.mitra.service.sessionService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.ValidateSessionRequest;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.ServiceRegistry;
import org.tax.mitra.service.sessionService.SessionServiceListener;

@Service
public class SessionServiceListenerImpl implements SessionServiceListener {
    private final ServiceRegistry serviceRegistry;

    @Autowired
    public SessionServiceListenerImpl(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Validates an active user session using the provided session request.
     *
     * <p>This method delegates the session validation logic to the appropriate
     * service implementation resolved dynamically from the {@link ServiceRegistry}
     * using {@link ServiceType#SESSION_VALIDATE}.
     *
     * <p>The underlying service is responsible for verifying:
     * <ul>
     *   <li>Whether the session exists in the cache/Redis</li>
     *   <li>Whether the session is active and not expired</li>
     *   <li>Whether the session state is valid for further processing</li>
     * </ul>
     *
     * <p>If the session is invalid or expired, the underlying service is expected
     * to throw a domain-specific exception (e.g., validation or session exception).
     *
     * @param request the session validation request containing session identifier
     *                 and related metadata required for validation
     *
     * @throws ClassCastException if the resolved service is not of the expected type
     * @throws RuntimeException if session validation fails due to business rules
     */
    @Override
    public void validateSession(ValidateSessionRequest request) {
        @SuppressWarnings("unchecked")
        CommonService<ValidateSessionRequest> service =
                (CommonService<ValidateSessionRequest>) serviceRegistry.getService(ServiceType.SESSION_VALIDATE);
        service.execute(request);
    }
}

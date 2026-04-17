package org.tax.mitra.service.gstService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.GstinRequestModel;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.ServiceRegistry;
import org.tax.mitra.service.gstService.GstinServiceListener;

@Service
public class GstinServiceListenerImpl implements GstinServiceListener {

    private final ServiceRegistry serviceRegistry;

    @Autowired
    public GstinServiceListenerImpl(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Delegates GSTIN fetch operation to the appropriate service implementation
     * resolved dynamically from ServiceRegistry.
     *
     * <p>The underlying service is responsible for:
     * <ul>
     *   <li>Validating GSTIN format</li>
     *   <li>Calling external API / DB / scraper</li>
     *   <li>Mapping response to internal model</li>
     *   <li>Caching response if required</li>
     * </ul>
     *
     * @param request GSTIN request model containing GSTIN and metadata
     */
    @Override
    public void fetchTaxPayer(GstinRequestModel request) {
        @SuppressWarnings("unchecked")
        CommonService<GstinRequestModel> service =
                (CommonService<GstinRequestModel>) serviceRegistry.getService(ServiceType.GSTIN_FETCH);
        service.execute(request);
    }
}
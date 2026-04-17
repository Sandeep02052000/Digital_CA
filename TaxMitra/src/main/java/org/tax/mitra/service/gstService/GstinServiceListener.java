package org.tax.mitra.service.gstService;

import org.tax.mitra.model.GstinRequestModel;

public interface GstinServiceListener {
    void fetchTaxPayer(GstinRequestModel model);
}

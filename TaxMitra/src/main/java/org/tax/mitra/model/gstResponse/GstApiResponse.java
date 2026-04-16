package org.tax.mitra.model.gstResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstApiResponse {

    @JsonProperty("error")
    private boolean error;

    @JsonProperty("data")
    private GstData data;
}
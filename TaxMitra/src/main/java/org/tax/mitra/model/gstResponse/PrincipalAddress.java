package org.tax.mitra.model.gstResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrincipalAddress {

    @JsonProperty("addr")
    private Address address;

    @JsonProperty("ntr")
    private String natureOfBusiness;
}
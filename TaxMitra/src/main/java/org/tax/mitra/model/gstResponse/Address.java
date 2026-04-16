package org.tax.mitra.model.gstResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    @JsonProperty("bnm")
    private String buildingName;

    @JsonProperty("st")
    private String street;

    @JsonProperty("loc")
    private String location;

    @JsonProperty("bno")
    private String buildingNumber;

    @JsonProperty("dst")
    private String district;

    @JsonProperty("stcd")
    private String stateCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("flno")
    private String floorNumber;

    @JsonProperty("lt")
    private String latitude;

    @JsonProperty("pncd")
    private String pincode;

    @JsonProperty("lg")
    private String longitude;
}
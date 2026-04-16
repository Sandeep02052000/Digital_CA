package org.tax.mitra.model.gstResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstData {

    @JsonProperty("stjCd")
    private String stateJurisdictionCode;

    @JsonProperty("lgnm")
    private String legalName;

    @JsonProperty("stj")
    private String stateJurisdiction;

    @JsonProperty("dty")
    private String taxpayerType;

    @JsonProperty("adadr")
    private List<Object> additionalAddresses;

    @JsonProperty("cxdt")
    private String cancellationDate;

    @JsonProperty("gstin")
    private String gstin;

    @JsonProperty("nba")
    private List<String> natureOfBusinessActivities;

    @JsonProperty("lstupdt")
    private String lastUpdatedDate;

    @JsonProperty("rgdt")
    private String registrationDate;

    @JsonProperty("ctb")
    private String constitutionOfBusiness;

    @JsonProperty("pradr")
    private PrincipalAddress principalAddress;

    @JsonProperty("tradeNam")
    private String tradeName;

    @JsonProperty("sts")
    private String status;

    @JsonProperty("ctjCd")
    private String centralJurisdictionCode;

    @JsonProperty("ctj")
    private String centralJurisdiction;
}
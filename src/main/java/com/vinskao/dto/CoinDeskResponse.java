package com.vinskao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoinDeskResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private Bpi bpi;

    @Data
    public static class Time {
        private String updated;
        private String updatedISO;
        private String updateduk;
    }

    @Data
    public static class Bpi {
        @JsonProperty("USD")
        private Currency USD;
        
        @JsonProperty("GBP")
        private Currency GBP;
        
        @JsonProperty("EUR")
        private Currency EUR;
    }

    @Data
    public static class Currency {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        @JsonProperty("rate_float")
        private Double rate_float;
    }
} 
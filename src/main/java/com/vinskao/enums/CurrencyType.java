package com.vinskao.enums;

public enum CurrencyType {
    USD("USD", "美元", "United States Dollar"),
    GBP("GBP", "英鎊", "British Pound Sterling"),
    EUR("EUR", "歐元", "Euro");

    private final String code;
    private final String chineseName;
    private final String englishName;

    CurrencyType(String code, String chineseName, String englishName) {
        this.code = code;
        this.chineseName = chineseName;
        this.englishName = englishName;
    }

    public String getCode() {
        return code;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public static CurrencyType fromCode(String code) {
        for (CurrencyType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown currency code: " + code);
    }
} 
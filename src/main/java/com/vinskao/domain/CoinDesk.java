package com.vinskao.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import lombok.Data;
import java.time.LocalDateTime;
import com.vinskao.enums.CurrencyType;

@Entity
@Table(name = "coin_desk")
@Data
public class CoinDesk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String updated;
    
    @Column(name = "updated_iso")
    private String updatedISO;
    
    private String updateduk;
    private String disclaimer;
    
    @Column(name = "chart_name")
    private String chartName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type")
    private CurrencyType currencyType;

    @Column(name = "rate")
    private String rate;

    @Column(name = "rate_float")
    private Double rateFloat;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "chinese_name")
    private String chineseName;
} 
package com.vinskao.service;

import com.vinskao.domain.CoinDesk;
import com.vinskao.dto.CoinDeskResponse;
import com.vinskao.repository.CoinDeskRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.BeanUtils;
import com.vinskao.enums.CurrencyType;
import java.time.LocalDateTime;

@Service
public class CoinDeskService {
    private static final Logger logger = LoggerFactory.getLogger(CoinDeskService.class);
    
    @Autowired
    private CoinDeskRepository coinDeskRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${coindesk.api.url}")
    private String apiUrl;

    @PostConstruct
    public void init() {
        logger.info("Application starting up, fetching initial CoinDesk data...");
        fetchAndSaveFromApi();
    }

    CoinDesk fetchAndSaveFromApi() {
        logger.info("Fetching data from API URL: {}", apiUrl);
        CoinDeskResponse response = restTemplate.getForObject(apiUrl, CoinDeskResponse.class);
        if (response == null) {
            logger.error("Failed to fetch data from API");
            throw new RuntimeException("Failed to fetch data from API");
        }

        // 創建 USD 記錄
        CoinDesk usdCoinDesk = createCoinDeskFromResponse(response, CurrencyType.USD, response.getBpi().getUSD());
        coinDeskRepository.save(usdCoinDesk);

        // 創建 GBP 記錄
        CoinDesk gbpCoinDesk = createCoinDeskFromResponse(response, CurrencyType.GBP, response.getBpi().getGBP());
        coinDeskRepository.save(gbpCoinDesk);

        // 創建 EUR 記錄
        CoinDesk eurCoinDesk = createCoinDeskFromResponse(response, CurrencyType.EUR, response.getBpi().getEUR());
        return coinDeskRepository.save(eurCoinDesk);
    }

    CoinDesk createCoinDeskFromResponse(CoinDeskResponse response, CurrencyType currencyType, CoinDeskResponse.Currency currency) {
        CoinDesk coinDesk = new CoinDesk();
        coinDesk.setUpdated(response.getTime().getUpdated());
        coinDesk.setUpdatedISO(response.getTime().getUpdatedISO());
        coinDesk.setUpdateduk(response.getTime().getUpdateduk());
        coinDesk.setDisclaimer(response.getDisclaimer());
        coinDesk.setChartName(response.getChartName());
        coinDesk.setCreatedAt(LocalDateTime.now());
        coinDesk.setUpdatedAt(LocalDateTime.now());
        
        coinDesk.setCurrencyType(currencyType);
        coinDesk.setRate(currency.getRate());
        coinDesk.setRateFloat(currency.getRate_float());
        coinDesk.setCurrencyName(currency.getDescription());
        coinDesk.setChineseName(currencyType.getChineseName());
        
        return coinDesk;
    }

    public CoinDesk saveCoinDesk(CoinDesk coinDesk) {
        return coinDeskRepository.save(coinDesk);
    }

    public CoinDesk getCoinDeskById(Long id) {
        return coinDeskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CoinDesk not found"));
    }

    public CoinDesk updateCoinDesk(Long id, CoinDesk coinDesk) {
        CoinDesk existingCoinDesk = getCoinDeskById(id);
        BeanUtils.copyProperties(coinDesk, existingCoinDesk, "id");
        return coinDeskRepository.save(existingCoinDesk);
    }

    public void deleteCoinDesk(Long id) {
        coinDeskRepository.deleteById(id);
    }

    public List<CoinDesk> getAllCoinDesks() {
        return coinDeskRepository.findAll();
    }
} 
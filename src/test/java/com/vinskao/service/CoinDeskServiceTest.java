package com.vinskao.service;

import com.vinskao.domain.CoinDesk;
import com.vinskao.dto.CoinDeskResponse;
import com.vinskao.dto.CoinDeskResponse.Bpi;
import com.vinskao.repository.CoinDeskRepository;
import com.vinskao.enums.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CoinDeskService 的單元測試類別。
 * 測試 CoinDeskService 的資料轉換、API 呼叫和資料庫操作功能。
 */
@ExtendWith(MockitoExtension.class)
public class CoinDeskServiceTest {

    @Mock
    private CoinDeskRepository coinDeskRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoinDeskService coinDeskService;

    private CoinDeskResponse mockResponse;
    private static final String TEST_API_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";

    /**
     * 測試前的設置工作。
     * 初始化測試所需的模擬資料，包括：
     * - 設置 API URL
     * - 創建模擬的 API 回應
     * - 設置 USD、GBP 和 EUR 的幣別資訊
     */
    @BeforeEach
    void setUp() {
        // 設置 API URL
        ReflectionTestUtils.setField(coinDeskService, "apiUrl", TEST_API_URL);
        
        // 準備模擬的 API 回應
        mockResponse = new CoinDeskResponse();
        CoinDeskResponse.Time time = new CoinDeskResponse.Time();
        time.setUpdated("Sep 2, 2024 07:07:20 UTC");
        time.setUpdatedISO("2024-09-02T07:07:20+00:00");
        time.setUpdateduk("Sep 2, 2024 at 08:07 BST");
        mockResponse.setTime(time);
        mockResponse.setDisclaimer("just for test");
        mockResponse.setChartName("Bitcoin");

        Bpi bpi = new Bpi();
        
        // USD 資料
        CoinDeskResponse.Currency usd = new CoinDeskResponse.Currency();
        usd.setCode("USD");
        usd.setRate("57,756.298");
        usd.setRate_float(57756.2984);
        usd.setDescription("United States Dollar");
        bpi.setUSD(usd);

        // GBP 資料
        CoinDeskResponse.Currency gbp = new CoinDeskResponse.Currency();
        gbp.setCode("GBP");
        gbp.setRate("43,984.02");
        gbp.setRate_float(43984.0203);
        gbp.setDescription("British Pound Sterling");
        bpi.setGBP(gbp);

        // EUR 資料
        CoinDeskResponse.Currency eur = new CoinDeskResponse.Currency();
        eur.setCode("EUR");
        eur.setRate("52,243.287");
        eur.setRate_float(52243.2865);
        eur.setDescription("Euro");
        bpi.setEUR(eur);

        mockResponse.setBpi(bpi);
    }

    /**
     * 測試從 API 回應創建 CoinDesk 實體的邏輯。
     * 驗證所有欄位是否正確轉換，包括：
     * - 時間資訊
     * - 幣別資訊
     * - 匯率資訊
     * - 幣別名稱
     */
    @Test
    void testCreateCoinDeskFromResponse() {
        // 測試 USD 幣別轉換
        CoinDesk usdCoinDesk = coinDeskService.createCoinDeskFromResponse(
            mockResponse, 
            CurrencyType.USD, 
            mockResponse.getBpi().getUSD()
        );

        assertNotNull(usdCoinDesk);
        assertEquals("Sep 2, 2024 07:07:20 UTC", usdCoinDesk.getUpdated());
        assertEquals("2024-09-02T07:07:20+00:00", usdCoinDesk.getUpdatedISO());
        assertEquals("Sep 2, 2024 at 08:07 BST", usdCoinDesk.getUpdateduk());
        assertEquals("just for test", usdCoinDesk.getDisclaimer());
        assertEquals("Bitcoin", usdCoinDesk.getChartName());
        assertEquals(CurrencyType.USD, usdCoinDesk.getCurrencyType());
        assertEquals("57,756.298", usdCoinDesk.getRate());
        assertEquals(57756.2984, usdCoinDesk.getRateFloat());
        assertEquals("United States Dollar", usdCoinDesk.getCurrencyName());
        assertEquals("美元", usdCoinDesk.getChineseName());
    }

    /**
     * 測試從 API 獲取資料並保存到資料庫的功能。
     * 驗證：
     * - API 呼叫是否成功
     * - 資料是否正確保存到資料庫
     * - 是否正確處理所有幣別（USD、GBP、EUR）
     */
    @SuppressWarnings("unchecked")
    @Test
    void testFetchAndSaveFromApi() {
        // 模擬 RestTemplate 的行為，使用更寬鬆的存根
        doReturn(mockResponse)
            .when(restTemplate)
            .getForObject(anyString(), any(Class.class));

        // 模擬 Repository 的保存行為
        CoinDesk savedCoinDesk = new CoinDesk();
        savedCoinDesk.setCurrencyType(CurrencyType.EUR);
        when(coinDeskRepository.save(any(CoinDesk.class)))
            .thenReturn(savedCoinDesk);

        // 執行測試
        CoinDesk result = coinDeskService.fetchAndSaveFromApi();

        // 驗證
        verify(coinDeskRepository, times(3)).save(any(CoinDesk.class));
        assertNotNull(result);
        assertEquals(CurrencyType.EUR, result.getCurrencyType());
    }

    /**
     * 測試獲取所有幣別資料的功能。
     * 驗證：
     * - 是否正確返回所有幣別的資料
     * - 返回的資料是否包含正確的幣別類型
     */
    @Test
    void testGetAllCoinDesks() {
        List<CoinDesk> mockList = Arrays.asList(
            createMockCoinDesk(1L, CurrencyType.USD),
            createMockCoinDesk(2L, CurrencyType.GBP),
            createMockCoinDesk(3L, CurrencyType.EUR)
        );
        when(coinDeskRepository.findAll()).thenReturn(mockList);

        List<CoinDesk> result = coinDeskService.getAllCoinDesks();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(CurrencyType.USD, result.get(0).getCurrencyType());
        assertEquals(CurrencyType.GBP, result.get(1).getCurrencyType());
        assertEquals(CurrencyType.EUR, result.get(2).getCurrencyType());
    }

    /**
     * 創建用於測試的模擬 CoinDesk 實體。
     *
     * @param id 實體 ID
     * @param currencyType 幣別類型
     * @return 模擬的 CoinDesk 實體
     */
    private CoinDesk createMockCoinDesk(Long id, CurrencyType currencyType) {
        CoinDesk coinDesk = new CoinDesk();
        coinDesk.setId(id);
        coinDesk.setCurrencyType(currencyType);
        coinDesk.setRate("1000.00");
        coinDesk.setRateFloat(1000.0);
        coinDesk.setCurrencyName("Test Currency");
        coinDesk.setChineseName("測試幣別");
        coinDesk.setCreatedAt(LocalDateTime.now());
        coinDesk.setUpdatedAt(LocalDateTime.now());
        return coinDesk;
    }
} 
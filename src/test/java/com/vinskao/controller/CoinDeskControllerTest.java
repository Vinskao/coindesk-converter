package com.vinskao.controller;

import com.vinskao.domain.CoinDesk;
import com.vinskao.service.CoinDeskService;
import com.vinskao.enums.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CoinDeskController 的單元測試類別。
 * 測試 CoinDeskController 的 REST API 端點功能，包括：
 * - 創建幣別資料
 * - 讀取幣別資料
 * - 更新幣別資料
 * - 刪除幣別資料
 * - 獲取所有幣別資料
 */
@ExtendWith(MockitoExtension.class)
public class CoinDeskControllerTest {

    @Mock
    private CoinDeskService coinDeskService;

    @InjectMocks
    private CoinDeskController coinDeskController;

    private CoinDesk mockCoinDesk;

    /**
     * 測試前的設置工作。
     * 初始化測試所需的模擬資料，包括：
     * - 基本資訊（ID、時間等）
     * - 幣別資訊（USD）
     * - 匯率資訊
     * - 幣別名稱
     */
    @BeforeEach
    void setUp() {
        mockCoinDesk = new CoinDesk();
        mockCoinDesk.setId(1L);
        mockCoinDesk.setUpdated("Sep 2, 2024 07:07:20 UTC");
        mockCoinDesk.setUpdatedISO("2024-09-02T07:07:20+00:00");
        mockCoinDesk.setUpdateduk("Sep 2, 2024 at 08:07 BST");
        mockCoinDesk.setDisclaimer("just for test");
        mockCoinDesk.setChartName("Bitcoin");
        mockCoinDesk.setCurrencyType(CurrencyType.USD);
        mockCoinDesk.setRate("57,756.298");
        mockCoinDesk.setRateFloat(57756.2984);
        mockCoinDesk.setCurrencyName("United States Dollar");
        mockCoinDesk.setChineseName("美元");
        mockCoinDesk.setCreatedAt(LocalDateTime.now());
        mockCoinDesk.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * 測試創建幣別資料的 API 端點。
     * 驗證：
     * - 回應是否包含正確的幣別資訊
     * - 回應的狀態碼是否正確
     * - 回應的內容是否符合預期
     */
    @Test
    void testCreateCoinDesk() {
        when(coinDeskService.saveCoinDesk(any(CoinDesk.class))).thenReturn(mockCoinDesk);

        ResponseEntity<CoinDesk> response = coinDeskController.createCoinDesk(mockCoinDesk);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(CurrencyType.USD, response.getBody().getCurrencyType());
        assertEquals("57,756.298", response.getBody().getRate());
        assertEquals("美元", response.getBody().getChineseName());
    }

    /**
     * 測試讀取單筆幣別資料的 API 端點。
     * 驗證：
     * - 回應是否包含正確的幣別資訊
     * - 回應的 ID 是否正確
     * - 回應的幣別類型是否正確
     */
    @Test
    void testReadCoinDesk() {
        when(coinDeskService.getCoinDeskById(1L)).thenReturn(mockCoinDesk);

        ResponseEntity<CoinDesk> response = coinDeskController.readCoinDesk(1L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(CurrencyType.USD, response.getBody().getCurrencyType());
    }

    /**
     * 測試更新幣別資料的 API 端點。
     * 驗證：
     * - 回應是否包含更新後的匯率資訊
     * - 回應的狀態碼是否正確
     * - 回應的內容是否符合預期
     */
    @Test
    void testUpdateCoinDesk() {
        CoinDesk updatedCoinDesk = new CoinDesk();
        updatedCoinDesk.setRate("58,000.00");
        updatedCoinDesk.setRateFloat(58000.00);
        when(coinDeskService.updateCoinDesk(eq(1L), any(CoinDesk.class))).thenReturn(updatedCoinDesk);

        ResponseEntity<CoinDesk> response = coinDeskController.updateCoinDesk(1L, updatedCoinDesk);

        assertNotNull(response.getBody());
        assertEquals("58,000.00", response.getBody().getRate());
        assertEquals(58000.00, response.getBody().getRateFloat());
    }

    /**
     * 測試刪除幣別資料的 API 端點。
     * 驗證：
     * - 服務層的刪除方法是否被調用
     * - 回應的狀態碼是否正確
     */
    @Test
    void testDeleteCoinDesk() {
        doNothing().when(coinDeskService).deleteCoinDesk(1L);

        ResponseEntity<Void> response = coinDeskController.deleteCoinDesk(1L);

        assertNotNull(response);
        verify(coinDeskService).deleteCoinDesk(1L);
    }

    /**
     * 測試獲取所有幣別資料的 API 端點。
     * 驗證：
     * - 回應是否包含所有幣別的資料
     * - 回應的幣別類型是否正確
     * - 回應的數量是否符合預期
     */
    @Test
    void testGetAllCoinDesks() {
        List<CoinDesk> mockList = Arrays.asList(
            createMockCoinDesk(1L, CurrencyType.USD),
            createMockCoinDesk(2L, CurrencyType.GBP),
            createMockCoinDesk(3L, CurrencyType.EUR)
        );
        when(coinDeskService.getAllCoinDesks()).thenReturn(mockList);

        ResponseEntity<List<CoinDesk>> response = coinDeskController.getAllCoinDesks();

        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals(CurrencyType.USD, response.getBody().get(0).getCurrencyType());
        assertEquals(CurrencyType.GBP, response.getBody().get(1).getCurrencyType());
        assertEquals(CurrencyType.EUR, response.getBody().get(2).getCurrencyType());
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
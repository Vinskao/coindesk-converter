package com.vinskao.controller;

import com.vinskao.domain.CoinDesk;
import com.vinskao.service.CoinDeskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "CoinDesk API")
@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {
    @Autowired
    private CoinDeskService coinDeskService;

    @ApiOperation("創建幣價資訊")
    @PostMapping("/create")
    public ResponseEntity<CoinDesk> createCoinDesk(@RequestBody CoinDesk coinDesk) {
        return ResponseEntity.ok(coinDeskService.saveCoinDesk(coinDesk));
    }

    @ApiOperation("讀取幣價資訊")
    @PostMapping("/read/{id}")
    public ResponseEntity<CoinDesk> readCoinDesk(@PathVariable Long id) {
        return ResponseEntity.ok(coinDeskService.getCoinDeskById(id));
    }

    @ApiOperation("更新幣價資訊")
    @PostMapping("/update/{id}")
    public ResponseEntity<CoinDesk> updateCoinDesk(@PathVariable Long id, @RequestBody CoinDesk coinDesk) {
        return ResponseEntity.ok(coinDeskService.updateCoinDesk(id, coinDesk));
    }

    @ApiOperation("刪除幣價資訊")
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCoinDesk(@PathVariable Long id) {
        coinDeskService.deleteCoinDesk(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("獲取所有幣價資訊")
    @PostMapping("/all")
    public ResponseEntity<List<CoinDesk>> getAllCoinDesks() {
        return ResponseEntity.ok(coinDeskService.getAllCoinDesks());
    }
} 
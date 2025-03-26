# CoinDesk 幣別轉換器

這是一個使用 Spring Boot 2 開發的幣別轉換服務，整合 CoinDesk API 來獲取即時幣別匯率資訊，並提供幣別轉換功能。

## 功能特點

- 整合 CoinDesk API 獲取即時匯率資訊
- 支援多種幣別（USD、GBP、EUR）
- 提供幣別的中英文名稱對應
- RESTful API 介面
- 使用 H2 資料庫儲存歷史匯率資料
- Swagger UI 提供 API 文件

## 技術棧

- Java 8
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- Swagger/OpenAPI

## 專案結構

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── vinskao/
│   │           ├── controller/    # API 控制器
│   │           ├── service/       # 業務邏輯層
│   │           ├── repository/    # 資料存取層
│   │           ├── domain/        # 領域模型
│   │           ├── dto/          # 資料傳輸物件
│   │           └── enums/        # 枚舉類型
│   └── resources/
│       ├── application.properties
│       └── schema.sql
└── test/
    └── java/
        └── com/
            └── vinskao/
                ├── controller/    # 控制器測試
                └── service/       # 服務層測試
```

## API 端點

- POST `/api/coindesk/create` - 創建幣別資料
- POST `/api/coindesk/read/{id}` - 讀取幣別資料
- POST `/api/coindesk/update/{id}` - 更新幣別資料
- POST `/api/coindesk/delete/{id}` - 刪除幣別資料
- POST `/api/coindesk/all` - 獲取所有幣別資料

## 運行專案

1. 確保已安裝 Java 8 和 Maven
2. 在專案根目錄執行：
   ```bash
   mvn spring-boot:run
   ```
3. 訪問 http://localhost:8080

## API 文件

- Swagger UI: http://localhost:8080/swagger-ui.html

## 資料庫

- H2 Console: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password

## 幣別支援

目前支援的幣別：
- USD (美元)
- GBP (英鎊)
- EUR (歐元)

## 開發團隊

- 開發者：Vinskao

## 授權

MIT License
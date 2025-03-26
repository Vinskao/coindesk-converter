# Coin API Handler

這是一個使用 Spring Boot 2 和 H2 資料庫的專案。

## 技術棧

- Java 8
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- Maven
- Lombok

## 專案結構

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── vinskao/
│   │           └── CoinApiHandlerApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
```

## 運行專案

1. 確保已安裝 Java 8 和 Maven
2. 在專案根目錄執行：
   ```bash
   mvn spring-boot:run
   ```
3. 訪問 http://localhost:8080

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password 


## Swagger
`http://localhost:8080/swagger-ui.html`
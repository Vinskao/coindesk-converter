CREATE TABLE IF NOT EXISTS coin_desk (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    updated VARCHAR(255),
    updated_iso VARCHAR(255),
    updateduk VARCHAR(255),
    disclaimer VARCHAR(255),
    chart_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    currency_type VARCHAR(10),
    rate VARCHAR(255),
    rate_float DOUBLE,
    currency_name VARCHAR(255),
    chinese_name VARCHAR(255)
);

CREATE INDEX idx_currency_type ON coin_desk(currency_type);
CREATE INDEX idx_updated_iso ON coin_desk(updated_iso); 
package com.vinskao.repository;

import com.vinskao.domain.CoinDesk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinDeskRepository extends JpaRepository<CoinDesk, Long> {
} 
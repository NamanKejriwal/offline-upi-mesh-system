package com.demo.upimesh.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop20ByOrderByIdDesc();
    Page<Transaction> findAllByOrderByIdDesc(Pageable pageable);
    boolean existsByPacketHash(String packetHash);
}

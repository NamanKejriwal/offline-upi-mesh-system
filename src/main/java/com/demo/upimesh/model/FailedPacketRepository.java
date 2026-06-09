package com.demo.upimesh.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for saving failed packets into the audit log.
 */
@Repository
public interface FailedPacketRepository extends JpaRepository<FailedPacket, Long> {
    List<FailedPacket> findTop10ByOrderByIdDesc();
}

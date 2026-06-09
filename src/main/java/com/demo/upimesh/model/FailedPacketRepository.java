package com.demo.upimesh.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for saving failed packets into the audit log.
 */
@Repository
public interface FailedPacketRepository extends JpaRepository<FailedPacket, Long> {
}

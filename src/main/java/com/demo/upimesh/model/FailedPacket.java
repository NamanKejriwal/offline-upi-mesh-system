package com.demo.upimesh.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Audit log of packets that failed the ingestion pipeline (tampered, stale, etc.).
 * Does NOT include DUPLICATE_DROPPED packets, as duplicates are expected and healthy.
 */
@Entity
@Table(
    name = "failed_packets",
    indexes = {
        @Index(name = "idx_failed_packet_hash", columnList = "packetHash")
    }
)
public class FailedPacket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String packetHash;

    @Column(nullable = false)
    private String bridgeNodeId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String ciphertext;

    @Column(nullable = false)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String errorDetails;

    @Column(nullable = false)
    private int hopCount;

    @Column(nullable = false)
    private Instant receivedAt;

    public FailedPacket() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPacketHash() { return packetHash; }
    public void setPacketHash(String packetHash) { this.packetHash = packetHash; }

    public String getBridgeNodeId() { return bridgeNodeId; }
    public void setBridgeNodeId(String bridgeNodeId) { this.bridgeNodeId = bridgeNodeId; }

    public String getCiphertext() { return ciphertext; }
    public void setCiphertext(String ciphertext) { this.ciphertext = ciphertext; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getErrorDetails() { return errorDetails; }
    public void setErrorDetails(String errorDetails) { this.errorDetails = errorDetails; }

    public int getHopCount() { return hopCount; }
    public void setHopCount(int hopCount) { this.hopCount = hopCount; }

    public Instant getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
}

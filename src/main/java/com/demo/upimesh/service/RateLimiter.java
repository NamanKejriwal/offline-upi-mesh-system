package com.demo.upimesh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory Token Bucket rate limiter.
 * This prevents API abuse by strictly limiting the number of requests per IP address.
 */
@Service
public class RateLimiter {

    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);

    private final Map<String, Bucket> clientBuckets = new ConcurrentHashMap<>();
    private final AtomicInteger blockedRequests = new AtomicInteger(0);

    // Configuration
    private static final int MAX_TOKENS = 10;
    private static final long REFILL_RATE_MS = 1000; // 1 token per second

    public boolean allowRequest(String clientIp) {
        Bucket bucket = clientBuckets.computeIfAbsent(clientIp, k -> new Bucket());
        boolean allowed = bucket.tryConsume();
        if (!allowed) {
            blockedRequests.incrementAndGet();
        }
        return allowed;
    }

    public int getBlockedCount() {
        return blockedRequests.get();
    }

    public int getActiveBucketCount() {
        return clientBuckets.size();
    }

    /**
     * Runs every 10 minutes to clean up buckets that haven't been touched in the last 30 minutes.
     * This prevents the ConcurrentHashMap from growing infinitely and causing an OOM leak.
     */
    @Scheduled(fixedRate = 600000)
    public void cleanupInactiveBuckets() {
        long cutoffTime = System.currentTimeMillis() - 1800000; // 30 minutes
        int removedCount = 0;

        Iterator<Map.Entry<String, Bucket>> iterator = clientBuckets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Bucket> entry = iterator.next();
            if (entry.getValue().getLastAccessTime() < cutoffTime) {
                iterator.remove();
                removedCount++;
            }
        }

        if (removedCount > 0) {
            log.info("RateLimiter cleanup: Removed {} inactive buckets. Active buckets remaining: {}",
                    removedCount, clientBuckets.size());
        }
    }

    private static class Bucket {
        private int tokens;
        private long lastRefillTimestamp;
        private long lastAccessTime;

        public Bucket() {
            this.tokens = MAX_TOKENS;
            this.lastRefillTimestamp = System.currentTimeMillis();
            this.lastAccessTime = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume() {
            this.lastAccessTime = System.currentTimeMillis();
            refillTokens();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refillTokens() {
            long now = System.currentTimeMillis();
            long timePassed = now - lastRefillTimestamp;
            int tokensToAdd = (int) (timePassed / REFILL_RATE_MS);

            if (tokensToAdd > 0) {
                this.tokens = Math.min(this.tokens + tokensToAdd, MAX_TOKENS);
                // Advance the timestamp by exactly the amount of time that produced tokens
                this.lastRefillTimestamp += (tokensToAdd * REFILL_RATE_MS);
            }
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }
    }
}

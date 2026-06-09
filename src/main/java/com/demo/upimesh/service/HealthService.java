package com.demo.upimesh.service;

import com.demo.upimesh.model.HealthStatus;
import com.demo.upimesh.service.VirtualDevice;
import com.demo.upimesh.model.FailedPacketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    @Autowired
    private MeshSimulatorService meshSimulator;

    @Autowired
    private FailedPacketRepository failedRepo;

    public HealthStatus checkHealth() {
        int activeBridges = (int) meshSimulator.getDevices().stream()
                .filter(VirtualDevice::hasInternet)
                .count();

        long failedPackets = failedRepo.count();

        String status;
        if (activeBridges == 0) {
            // No settlement path available. A real outage.
            status = "CRITICAL";
        } else if (failedPackets > 0) {
            // System is operational, but anomalies (tampering/stale packets) exist.
            status = "DEGRADED";
        } else {
            // Settlement path is available and no anomalies.
            status = "HEALTHY";
        }

        return new HealthStatus(status, activeBridges, true);
    }
}

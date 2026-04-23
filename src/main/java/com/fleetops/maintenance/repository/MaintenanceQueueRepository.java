package com.fleetops.maintenance.repository;

import com.fleetops.maintenance.entity.MaintenanceQueue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaintenanceQueueRepository extends JpaRepository<MaintenanceQueue, Long> {
    @EntityGraph(attributePaths = "tasks")
    Optional<MaintenanceQueue> findByUsername(String username);
}


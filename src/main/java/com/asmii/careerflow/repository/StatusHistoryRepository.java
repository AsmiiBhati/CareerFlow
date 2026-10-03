package com.asmii.careerflow.repository;

import com.asmii.careerflow.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, UUID>{
    List<StatusHistory> findByApplicationIdOrderByChangedAtAsc(UUID applicationId);
}

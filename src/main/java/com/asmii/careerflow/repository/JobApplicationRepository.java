package com.asmii.careerflow.repository;

import com.asmii.careerflow.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID>, JpaSpecificationExecutor<JobApplication> {
    // Fetch the user in the same query so mapping a page of results never triggers N+1 selects
    @Override
    @EntityGraph(attributePaths = "user")
    Page<JobApplication> findAll(Specification<JobApplication> spec, Pageable pageable);
}

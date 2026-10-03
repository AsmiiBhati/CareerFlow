package com.asmii.careerflow.spec;

import com.asmii.careerflow.entity.JobApplication;
import com.asmii.careerflow.enums.ApplicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Builds the multi-field search filter; every parameter is optional. */
public final class ApplicationSpecs {
    private ApplicationSpecs() {}

    public static Specification<JobApplication> filter(UUID userId, ApplicationStatus status, String company, String location) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null)
            {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (status != null)
            {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (company != null && !company.isBlank())
            {
                predicates.add(cb.like(cb.lower(root.get("companyName")),"%" + company.trim().toLowerCase() + "%"));
            }
            if (location != null && !location.isBlank())
            {
                predicates.add(cb.like(cb.lower(root.get("location")),"%" + location.trim().toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
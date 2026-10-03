package com.asmii.careerflow.dto;

import com.asmii.careerflow.entity.JobApplication;
import com.asmii.careerflow.entity.StatusHistory;
import com.asmii.careerflow.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ApplicationDtos {
    private ApplicationDtos() {}

    public record CreateRequest(@NotNull UUID userId, @NotBlank @Size(max = 150) String companyName, @NotBlank @Size(max = 150) String role, @Size(max = 150) String location, LocalDateTime appliedAt) {}

    public record UpdateRequest(@NotBlank @Size(max = 150) String companyName, @NotBlank @Size(max = 150) String role, @Size(max = 150) String location, LocalDateTime appliedAt) {}

    public record StatusChangeRequest(@NotNull ApplicationStatus status) {}

    public record Response(UUID id, UUID userId, String companyName, String role, String location, ApplicationStatus status, LocalDateTime appliedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        public static Response from(JobApplication a) {
            return new Response(a.getId(), a.getUser().getId(), a.getCompanyName(), a.getRole(), a.getLocation(), a.getStatus(), a.getAppliedAt(), a.getCreatedAt(), a.getUpdatedAt());
        }
    }

    public record HistoryResponse(UUID id, UUID applicationId, ApplicationStatus fromStatus, ApplicationStatus toStatus, LocalDateTime changedAt) {
        public static HistoryResponse from(StatusHistory h) {
            return new HistoryResponse(h.getId(), h.getApplication().getId(), h.getFromStatus(), h.getToStatus(), h.getChangedAt());
        }
    }
}

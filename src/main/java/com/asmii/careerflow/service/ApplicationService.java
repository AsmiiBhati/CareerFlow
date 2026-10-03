package com.asmii.careerflow.service;

import com.asmii.careerflow.dto.ApplicationDtos.CreateRequest;
import com.asmii.careerflow.dto.ApplicationDtos.HistoryResponse;
import com.asmii.careerflow.dto.ApplicationDtos.Response;
import com.asmii.careerflow.dto.ApplicationDtos.UpdateRequest;
import com.asmii.careerflow.dto.PageResponse;
import com.asmii.careerflow.entity.JobApplication;
import com.asmii.careerflow.entity.StatusHistory;
import com.asmii.careerflow.entity.User;
import com.asmii.careerflow.enums.ApplicationStatus;
import com.asmii.careerflow.exception.InvalidStatusTransitionException;
import com.asmii.careerflow.exception.ResourceNotFoundException;
import com.asmii.careerflow.repository.JobApplicationRepository;
import com.asmii.careerflow.repository.StatusHistoryRepository;
import com.asmii.careerflow.repository.UserRepository;
import com.asmii.careerflow.spec.ApplicationSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final JobApplicationRepository applications;
    private final StatusHistoryRepository histories;
    private final UserRepository users;

    @Transactional
    public Response create(CreateRequest req)
    {
        User user = users.findById(req.userId()).orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.userId()));
        JobApplication app = new JobApplication();
        app.setUser(user);
        app.setCompanyName(req.companyName().trim());
        app.setRole(req.role().trim());
        app.setLocation(req.location());
        app.setStatus(ApplicationStatus.APPLIED);
        app.setAppliedAt(req.appliedAt() != null ? req.appliedAt() : LocalDateTime.now());
        JobApplication saved = applications.save(app);
        histories.save(StatusHistory.of(saved, null, ApplicationStatus.APPLIED));
        return Response.from(saved);
    }

    @Transactional(readOnly = true)
    public Response get(UUID id)
    {
        return Response.from(find(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<Response> search(UUID userId, ApplicationStatus status, String company, String location, Pageable pageable) {
        Page<JobApplication> page = applications.findAll(ApplicationSpecs.filter(userId, status, company, location), pageable);
        return PageResponse.from(page.map(Response::from));
    }

    @Transactional
    public Response update(UUID id, UpdateRequest req)
    {
        JobApplication app = find(id);
        app.setCompanyName(req.companyName().trim());
        app.setRole(req.role().trim());
        app.setLocation(req.location());
        if (req.appliedAt() != null)
        {
            app.setAppliedAt(req.appliedAt());
        }
        return Response.from(applications.saveAndFlush(app));
    }

    @Transactional
    public void delete(UUID id)
    {
        applications.delete(find(id)); // status_history / interviews rows go via ON DELETE CASCADE
    }

    //Business rule: only transitions allowed by ApplicationStatus.allowedNext(); history written atomically.
    @Transactional
    public Response changeStatus(UUID id, ApplicationStatus next)
    {
        JobApplication app = find(id);
        ApplicationStatus current = app.getStatus();
        if (!current.canTransitionTo(next))
        {
            throw new InvalidStatusTransitionException(current, next);
        }
        app.setStatus(next);
        JobApplication saved = applications.saveAndFlush(app);
        histories.save(StatusHistory.of(saved, current, next));
        return Response.from(saved);
    }

    @Transactional(readOnly = true)
    public List<HistoryResponse> history(UUID id)
    {
        if (!applications.existsById(id))
        {
            throw new ResourceNotFoundException("Application not found: " + id);
        }
        return histories.findByApplicationIdOrderByChangedAtAsc(id).stream().map(HistoryResponse::from).toList();
    }

    private JobApplication find(UUID id) {
        return applications.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application not found: " + id));
    }

}

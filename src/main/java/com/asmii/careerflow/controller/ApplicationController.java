package com.asmii.careerflow.controller;

import com.asmii.careerflow.dto.ApplicationDtos.CreateRequest;
import com.asmii.careerflow.dto.ApplicationDtos.HistoryResponse;
import com.asmii.careerflow.dto.ApplicationDtos.Response;
import com.asmii.careerflow.dto.ApplicationDtos.StatusChangeRequest;
import com.asmii.careerflow.dto.ApplicationDtos.UpdateRequest;
import com.asmii.careerflow.dto.PageResponse;
import com.asmii.careerflow.enums.ApplicationStatus;
import com.asmii.careerflow.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody CreateRequest req)
    {
        return service.create(req);
    }

    @GetMapping
    public PageResponse<Response> search(@RequestParam(required = false) UUID userId, @RequestParam(required = false) ApplicationStatus status, @RequestParam(required = false) String company, @RequestParam(required = false) String location, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return service.search(userId, status, company, location, pageable);
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable UUID id)
    {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Response update(@PathVariable UUID id, @Valid @RequestBody UpdateRequest req)
    {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id)
    {
        service.delete(id);
    }

    @PatchMapping("/{id}/status")
    public Response changeStatus(@PathVariable UUID id, @Valid @RequestBody StatusChangeRequest req)
    {
        return service.changeStatus(id, req.status());
    }

    @GetMapping("/{id}/history")
    public List<HistoryResponse> history(@PathVariable UUID id)
    {
        return service.history(id);
    }
}

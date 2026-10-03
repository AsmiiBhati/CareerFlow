package com.asmii.careerflow.enums;

import java.util.EnumSet;
import java.util.Set;

public enum ApplicationStatus {
    APPLIED,
    SCREENING,
    INTERVIEW,
    OFFER,
    ACCEPTED,
    REJECTED,
    WITHDRAWN;

    public Set<ApplicationStatus> allowedNext() {
        return switch (this)
        {
            case APPLIED   -> EnumSet.of(SCREENING, REJECTED, WITHDRAWN);
            case SCREENING -> EnumSet.of(INTERVIEW, REJECTED, WITHDRAWN);
            case INTERVIEW -> EnumSet.of(OFFER, REJECTED, WITHDRAWN);
            case OFFER     -> EnumSet.of(ACCEPTED, REJECTED, WITHDRAWN);
            case ACCEPTED, REJECTED, WITHDRAWN -> EnumSet.noneOf(ApplicationStatus.class);
        };
    }

    public boolean canTransitionTo(ApplicationStatus next)
    {
        return allowedNext().contains(next);
    }
}


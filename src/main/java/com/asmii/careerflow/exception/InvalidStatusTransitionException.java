package com.asmii.careerflow.exception;

import com.asmii.careerflow.enums.ApplicationStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(ApplicationStatus from, ApplicationStatus to) {
        super("Cannot move application from " + from + " to " + to + ". Allowed next statuses: " + from.allowedNext());
    }
}

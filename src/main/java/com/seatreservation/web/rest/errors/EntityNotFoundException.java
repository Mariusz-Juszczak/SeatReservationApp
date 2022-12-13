package com.seatreservation.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EntityNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Long id, Class<?> entityClass) {
        super(null, entityClass.getSimpleName() + " with id: " + id + " not found", Status.BAD_REQUEST);
    }
}

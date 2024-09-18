package vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions;

import jakarta.validation.ConstraintViolation;

import java.util.Set;
import java.util.stream.Collectors;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        this(enrichErrorMessages(constraintViolations));
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    private static String enrichErrorMessages(Set<? extends ConstraintViolation<?>> constraintViolations) {
        return constraintViolations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}

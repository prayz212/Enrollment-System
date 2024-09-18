package vidiec.hands_on_practice.enrollment.lab.poc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.InvalidArgumentException;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.NotFoundException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFound(NotFoundException exception, WebRequest request) {
        log.error(exception.getMessage());
        ErrorMessage message = new ErrorMessage(exception.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity<ErrorMessage> invalidArgument(InvalidArgumentException exception, WebRequest request) {
        log.error(exception.getMessage());
        ErrorMessage message = new ErrorMessage(exception.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}

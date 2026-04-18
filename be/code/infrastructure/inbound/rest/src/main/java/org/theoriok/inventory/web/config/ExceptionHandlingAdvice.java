package org.theoriok.inventory.web.config;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionHandlingAdvice {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    @ExceptionHandler(Throwable.class)
    ResponseEntity<ProblemDetail> onException(Throwable exception) {
        LOGGER.error("Unexpected error", exception);
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, "Something went wrong")).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        var problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), "Validation failed");
        var errors = ex.getBindingResult().getFieldErrors().stream()
            .sorted(comparing(FieldError::getDefaultMessage))
            .collect(groupingBy(FieldError::getField, mapping(FieldError::getDefaultMessage, joining(", "))));
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.of(problemDetail).build();
    }
}
